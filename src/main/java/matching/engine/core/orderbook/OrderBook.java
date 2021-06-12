package matching.engine.core.orderbook;

import matching.engine.core.api.OrderRequest;
import matching.engine.core.common.order.IOrder;
import matching.engine.core.common.order.Order;
import matching.engine.core.common.order.OrderSide;
import matching.engine.core.common.trade.Trade;
import matching.engine.core.orderbook.events.OrderBookEvent;
import matching.engine.core.orderbook.events.RejectReason;

import java.util.*;
import java.util.stream.Collectors;

public class OrderBook implements IOrderBook {

    private final long instrument;
    private final NavigableMap<Long, OrderBookEntity> bids;
    private final NavigableMap<Long, OrderBookEntity> asks;
    private final Set<Long> activeOrderSet;

    private final List<IOrderBookListener> eventListeners;

    public OrderBook(long instrument) {
        this.instrument = instrument;
        this.bids = new TreeMap<>(Collections.reverseOrder());
        this.asks = new TreeMap<>();
        this.eventListeners = new ArrayList<>();
        this.activeOrderSet = new HashSet<>();
    }

    @Override
    public void newOrder(OrderRequest orderRequest) {

        if (orderRequest.getInstrument() != this.instrument) {
            OrderBookEvent.Reject rejEvent = new OrderBookEvent.Reject(orderRequest, RejectReason.BAD_SYMBOL);
            notify(rejEvent);
            return;
        }

        if (this.activeOrderSet.contains(orderRequest.getOrderId())) {
            OrderBookEvent.Reject rejEvent = new OrderBookEvent.Reject(orderRequest, RejectReason.DUPLICATE_ORDER);
            notify(rejEvent);
            return;
        }

        Order matchable = Order.orderFromReq(orderRequest);
        switch (orderRequest.getOrderType()) {
            case MARKET:
                OrderBookEvent.Match matchEventMarket = tryFill(matchable, true);
                if (matchable.getRemainingSize() > 0) {
                    placeOrderIntoBook(matchable);
                }
                if (matchEventMarket != null) {
                    this.notify(matchEventMarket);
                    return;
                }
            case LIMIT:
                OrderBookEvent.Match matchEventLimit = tryFill(matchable, false);
                if (matchable.getRemainingSize() > 0) {
                    placeOrderIntoBook(matchable);
                }
                if (matchEventLimit != null) {
                    this.notify(matchEventLimit);
                    return;
                }
            case FOK:
                break;
            default:
                OrderBookEvent.Reject rejEvent = new OrderBookEvent.Reject(orderRequest, RejectReason.ORDER_TYPE_NOT_SUPPORTED);
                notify(rejEvent);
        }

    }

    private OrderBookEvent.Match tryFill(Order matchable, boolean isMarketOrder) {
        // TODO: deal with case where there are no quotes on opposite side
        NavigableMap<Long, OrderBookEntity> quotes = getOppositeSideQuotes(matchable.getSide());

        List<Long> emptyPriceLevels = new ArrayList<>();

        List<Trade> trades;
        List<Trade> allTrades = new ArrayList<>();
        for (OrderBookEntity entity: quotes.values()) {
            trades = entity.matchOrders(matchable, isMarketOrder);

            if (trades.isEmpty()) {
                break;
            } else {
                if (entity.getTotalSize() == 0L) {
                    emptyPriceLevels.add(entity.getPrice());
                }
                allTrades.addAll(trades);
            }
        }

        if (allTrades.isEmpty()) {
            return null;
        } else {
            emptyPriceLevels.forEach(quotes::remove);
            return new OrderBookEvent.Match(allTrades);
        }

    }

    private NavigableMap<Long, OrderBookEntity> getSameSideQuotes(OrderSide side) {
        return side.equals(OrderSide.BID) ? this.bids : this.asks;
    }

    private NavigableMap<Long, OrderBookEntity> getOppositeSideQuotes(OrderSide side) {
        return side.equals(OrderSide.BID) ? this.asks : this.bids;
    }

    private void placeOrderIntoBook(Order order) {
        NavigableMap<Long, OrderBookEntity> quotes = getSameSideQuotes(order.getSide());
        OrderBookEntity entity = quotes.getOrDefault(order.getPrice(), new OrderBookEntity(order.getPrice(), order.getSide()));
        entity.addOrder(order);
        quotes.put(order.getPrice(), entity);
        this.activeOrderSet.add(order.getOrderId());
    }

    @Override
    public void amendOrder(OrderRequest orderRequest) {
        /*  Possible Events:
         *  - reject due to bad instrument
         *  - reject due unavailable orderId / already filled ?
         *  - amend success event along with zero or more trades
        */

    }

    @Override
    public void cancelOrder(OrderRequest orderRequest) {
        if (orderRequest.getInstrument() != this.instrument) {
            OrderBookEvent.Reject rejEvent = new OrderBookEvent.Reject(orderRequest, RejectReason.BAD_SYMBOL);
            notify(rejEvent);
            return;
        }

        if (!this.activeOrderSet.contains(orderRequest.getOrderId())) {
            OrderBookEvent.Reject rejEvent = new OrderBookEvent.Reject(orderRequest, RejectReason.ORDER_NOT_FOUND);
            notify(rejEvent);
            return;
        }

        NavigableMap<Long, OrderBookEntity> quotes = getSameSideQuotes(orderRequest.getSide());
        OrderBookEntity entity = quotes.get(orderRequest.getPrice());
        boolean tryCancel = entity.removeOrder(orderRequest.getOrderId());
        if (tryCancel) {
            this.activeOrderSet.remove(orderRequest.getOrderId());
            OrderBookEvent.CancelSuccess successEvent = new OrderBookEvent.CancelSuccess(orderRequest);
            notify(successEvent);
        } else {
            // some warning
            OrderBookEvent.Reject rejEvent = new OrderBookEvent.Reject(orderRequest, RejectReason.ORDER_NOT_FOUND);
            notify(rejEvent);
        }

    }

    @Override
    public List<OrderBookEntity.Level> getBids() {
        return this.bids.values().stream().map(OrderBookEntity::getLevelCopy).collect(Collectors.toList());
    }

    @Override
    public List<OrderBookEntity.Level> getAsks() {
        return this.asks.values().stream().map(OrderBookEntity::getLevelCopy).collect(Collectors.toList());
    }

    @Override
    public long getInstrument() {
        return this.instrument;
    }

    @Override
    public Collection<IOrder> getUserOrders(long userId) {
        return null;
    }

    @Override
    public void addListener(IOrderBookListener orderBookListener) {
        this.eventListeners.add(orderBookListener);
    }

    @Override
    public void removeListener(IOrderBookListener orderBookListener) {
        this.eventListeners.remove(orderBookListener);
    }

    @Override
    public void removeAllListeners() {
        this.eventListeners.clear();
    }

    private void notify(OrderBookEvent event) {
        this.eventListeners.forEach(listener -> listener.process(event));
    }

}


