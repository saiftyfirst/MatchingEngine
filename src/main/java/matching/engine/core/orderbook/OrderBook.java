package matching.engine.core.orderbook;

import matching.engine.core.api.OrderRequest;
import matching.engine.core.common.order.IOrder;
import matching.engine.core.common.order.Order;
import matching.engine.core.common.order.OrderSide;
import matching.engine.core.common.trade.Trade;
import matching.engine.core.orderbook.events.OrderBookEvent;
import matching.engine.core.orderbook.events.RejectReason;

import java.util.*;

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

        switch (orderRequest.getOrderType()) {
            case MARKET:
                OrderBookEvent.Match matchEventMarket = fillMarketOrder(orderRequest);
                if (matchEventMarket != null) {
                    return;
                }
            case LIMIT:
                OrderBookEvent.Match matchEventLimit = tryFillLimitOrder(orderRequest);
                if (matchEventLimit != null) {
                    return;
                }
            case FOK:
                break;
            default:
                OrderBookEvent.Reject rejEvent = new OrderBookEvent.Reject(orderRequest, RejectReason.ORDER_TYPE_NOT_SUPPORTED);
                notify(rejEvent);
        }

    }

    private OrderBookEvent.Match fillMarketOrder(OrderRequest orderRequest) {
        // TODO: deal with case where there are no quotes on opposite side
        Order matchable = Order.orderFromReq(orderRequest);
        NavigableMap<Long, OrderBookEntity> quotes = getOppositeQuotes(orderRequest.getSide());

        List<Long> emptyPriceLevels = new ArrayList<>();

        List<Trade> trades;
        List<Trade> allTrades = new ArrayList<>();
        for (OrderBookEntity entity: quotes.values()) {
            trades = entity.matchOrders(matchable);

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

    private OrderBookEvent.Match tryFillLimitOrder(OrderRequest orderRequest) {
        return null;
    }

    private OrderBookEvent.MatchWithReject tryFillFOKOrder(OrderRequest orderRequest) {
        return null;
    }

    private NavigableMap<Long, OrderBookEntity> getOppositeQuotes(OrderSide side) {
        return side.equals(OrderSide.BID) ? this.asks : this.bids;
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
        /*  Possible Events:
         *  - reject due to bad instrument
         *  - reject due unavailable orderId / already filled ?
         *  - successful cancel
         */
    }

    @Override
    public Collection<IOrder> getBids() {
        return Collections.emptyList();
    }

    @Override
    public Collection<IOrder> getAsks() {
        return Collections.emptyList();
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


