package matching.engine.core.orderbook;

import matching.engine.core.order.Order;
import matching.engine.core.order.Side;
import matching.engine.core.trade.FillType;
import matching.engine.core.trade.Trade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OrderChainProRata extends AbstractOrderChain {

    LinkedHashMap<Long, Order> orderChain;

    public OrderChainProRata(long price) {
        super(price, 0);
        this.orderChain = new LinkedHashMap<>();
    }

    @Override
    public void chainOrder(Order chainable) {
        this.totalSize += chainable.getRemainingSize();
        orderChain.put(chainable.getOrderId(), chainable);
    }

    @Override
    public boolean removeOrder(long orderId) {
        Order removable = this.orderChain.get(orderId);
        if (removable != null) {
            this.orderChain.remove(orderId);
            this.totalSize -= removable.getRemainingSize();
            return true;
        }
        return false;
    }

    @Override
    public boolean amendOrderSize(long orderId, long newSize) {
        Order amendable = this.orderChain.get(orderId);
        if (amendable != null) {
            long filled = amendable.getSize() - amendable.getRemainingSize();
            if (filled >= newSize) {
                this.orderChain.remove(orderId);
                this.totalSize -= amendable.getRemainingSize();
            } else {
                this.totalSize -= amendable.getRemainingSize();
                amendable.setSize(newSize);
                amendable.setRemainingSize(newSize - filled);
                this.totalSize += amendable.getRemainingSize();
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Trade> getMatches(Order matchable) {
        if ((matchable.getSide().equals(Side.ASK) && (matchable.getPrice() <= this.price)) ||
                (matchable.getSide().equals(Side.BID)) && (matchable.getPrice() >= this.price)) {
            return produceTrades(matchable);
        }
        return null;
    }

    private List<Trade> produceTrades(Order matchable) {
        List<Trade> trades = new ArrayList<>();
        long matchableSize = matchable.getRemainingSize();

        if (matchableSize > this.totalSize) {
            trades.addAll(
                    this.orderChain.values().stream().map(order -> new Trade(
                            this.price,
                            order.getRemainingSize(),
                            matchable.getOrderId(),
                            matchable.getUserId(),
                            matchable.getSide(),
                            order.getRemainingSize() < matchable.getSize() ? FillType.PARTIAL : FillType.FULL,
                            order.getOrderId(),
                            order.getUserId(),
                            order.getSide(),
                            order.getRemainingSize() < order.getSize() ? FillType.PARTIAL : FillType.FULL,
                            new Timestamp(System.currentTimeMillis()).getTime()
                    )).collect(Collectors.toList())
            );
            matchable.setRemainingSize(matchableSize - this.totalSize);

            this.totalSize = 0;
            this.orderChain.clear();

            return trades;
        }

        long tradeSize;
        long cumulativeTradeSize = 0L;
        long toFill = matchableSize;

        List<Long> ordersToRemove = new ArrayList<>();

        for (Order order: this.orderChain.values()) {

            tradeSize = (order.getRemainingSize() * matchableSize) / totalSize;
            tradeSize = Math.min(tradeSize, toFill);
            cumulativeTradeSize += tradeSize;

            trades.add(new Trade(
                    this.price,
                    tradeSize,
                    matchable.getOrderId(),
                    matchable.getUserId(),
                    matchable.getSide(),
                    tradeSize < matchable.getSize() ? FillType.PARTIAL : FillType.FULL,
                    order.getOrderId(),
                    order.getUserId(),
                    order.getSide(),
                    tradeSize < order.getSize() ? FillType.PARTIAL : FillType.FULL,
                    new Timestamp(System.currentTimeMillis()).getTime()
            ));

            toFill -= tradeSize;

            if (tradeSize < order.getRemainingSize()) {
                order.setRemainingSize(order.getRemainingSize() - tradeSize);
            } else {
                ordersToRemove.add(order.getOrderId());
            }
        }

        this.totalSize -= cumulativeTradeSize;
        ordersToRemove.forEach(orderId -> this.orderChain.remove(orderId));

        return trades;
    }

}
