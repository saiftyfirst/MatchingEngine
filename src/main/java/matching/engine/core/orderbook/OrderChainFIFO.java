package matching.engine.core.orderbook;

import matching.engine.core.order.Order;
import matching.engine.core.order.Side;
import matching.engine.core.trade.FillType;
import matching.engine.core.trade.Trade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderChainFIFO extends AbstractOrderChain {

    LinkedHashMap<Long, Order> orderChain;

    public OrderChainFIFO(long price) {
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

        long tradeSize;

        long matchableRemaining = matchable.getRemainingSize();
        long chainOrderRemaining;

        List<Long> ordersToRemove = new ArrayList<>();

        for (Order order : this.orderChain.values()) {

            if (matchableRemaining == 0L) {
                break;
            }

            chainOrderRemaining = order.getRemainingSize();
            tradeSize = Math.min(matchableRemaining, chainOrderRemaining);

            this.totalSize -= tradeSize;
            matchableRemaining -= tradeSize;
            chainOrderRemaining -= tradeSize;

            if (chainOrderRemaining == 0L) {
                ordersToRemove.add(order.getOrderId());
            } else {
                order.setRemainingSize(chainOrderRemaining);
            }

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
        }
        ordersToRemove.forEach(orderId -> this.orderChain.remove(orderId));

        return trades;
    }

}
