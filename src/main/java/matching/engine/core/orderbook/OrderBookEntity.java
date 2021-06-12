package matching.engine.core.orderbook;

import lombok.Data;
import matching.engine.core.common.order.Order;
import matching.engine.core.common.order.OrderSide;
import matching.engine.core.common.trade.Trade;
import matching.engine.core.orderbook.events.FillType;

import java.util.*;

class OrderBookEntity {

    private Level level;
    private OrderSide side;
    private final LinkedHashMap<Long, Order> orderChain;

    OrderBookEntity(long price, OrderSide side) {
        this.level = new Level(price, 0L);
        this.side = side;
        orderChain = new LinkedHashMap<>();
    }

    OrderBookEntity(long price, OrderSide side, long totalSize) {
        this.level = new Level(price, totalSize);
        this.side = side;
        orderChain = new LinkedHashMap<>();
    }

    public long getPrice() {
        return this.level.price;
    }

    long getTotalSize() {
        return this.level.totalSize;
    }

    void addOrder(Order order) {
        this.level.totalSize += order.getSize();
        this.orderChain.put(order.getOrderId(), order);
    }

    boolean removeOrder(long orderId) {
        if (this.orderChain.containsKey(orderId)) {
            Order removable = this.orderChain.remove(orderId);
            this.level.totalSize -= removable.getSize();
            return true;
        }
        return false;
    }

    boolean reduceSize(long orderId, long newSize) {
        if (this.orderChain.containsKey(orderId)) {
            Order reducible = this.orderChain.remove(orderId);
            this.level.totalSize -= reducible.getSize();
            reducible.setSize(newSize);
            this.level.totalSize += newSize;
            return true;
        }
        return false;
    }

    List<Trade> matchOrders(Order matchable, boolean isMarketOrder) {

        // TODO: ONLY WORKS FOR LIMIT ORDERS

        List<Trade> trades = new ArrayList<>();
        if (matchable.getSide().equals(getOppositeSide())) {

            long matchablePrice = matchable.getPrice();
            long toFill = matchable.getRemainingSize();

            List<Long> ordersToRemove = new ArrayList<>();

            long tradeSize;
            if (this.side.equals(OrderSide.ASK)) {
                if (!isMarketOrder && matchablePrice < this.level.price) {
                    return trades;
                }

                for (Map.Entry<Long, Order> entry: this.orderChain.entrySet()) {
                    if (toFill == 0) break;

                    tradeSize = Math.min(entry.getValue().getSize(), toFill);
                    entry.getValue().setRemainingSize(entry.getValue().getRemainingSize() - tradeSize);
                    this.level.totalSize -= tradeSize;
                    toFill -= tradeSize;
                    matchable.setRemainingSize(toFill);

                    trades.add(new Trade(
                            this.level.price,
                            tradeSize,
                            matchable.getUserId(),
                            matchable.getOrderId(),
                            tradeSize == matchable.getSize() ? FillType.FULL : FillType.PARTIAL,
                            toFill,
                            entry.getValue().getUserId(),
                            entry.getValue().getOrderId(),
                            entry.getValue().getRemainingSize() == 0 ? FillType.FULL : FillType.PARTIAL,
                            entry.getValue().getRemainingSize(),
                            123
                    ));

                    if (entry.getValue().getRemainingSize() == 0L) {
                        ordersToRemove.add(entry.getKey());
                    }
                }

                ordersToRemove.forEach(this.orderChain::remove);
                return trades;

            } else if (side.equals(OrderSide.BID)) {
                if (!isMarketOrder && matchablePrice > this.level.price) {
                    return Collections.emptyList();
                }

                for (Map.Entry<Long, Order> entry: this.orderChain.entrySet()) {
                    if (toFill == 0) break;

                    tradeSize = Math.min(entry.getValue().getSize(), toFill);
                    entry.getValue().setRemainingSize(entry.getValue().getRemainingSize() - tradeSize);
                    this.level.totalSize -= tradeSize;
                    toFill -= tradeSize;
                    matchable.setRemainingSize(toFill);

                    trades.add(new Trade(
                            this.level.price,
                            tradeSize,
                            entry.getValue().getUserId(),
                            entry.getValue().getOrderId(),
                            entry.getValue().getRemainingSize() == 0 ? FillType.FULL : FillType.PARTIAL,
                            entry.getValue().getRemainingSize(),
                            matchable.getUserId(),
                            matchable.getOrderId(),
                            tradeSize == matchable.getSize() ? FillType.FULL : FillType.PARTIAL,
                            toFill,
                            123
                    ));

                    if (entry.getValue().getRemainingSize() == 0L) {
                        ordersToRemove.add(entry.getKey());
                    }
                }
                ordersToRemove.forEach(this.orderChain::remove);
                return trades;
            }
        }
        return trades;
    }

    public Level getLevelCopy() {
        return (Level) this.level.clone();
    }

    private OrderSide getOppositeSide() {
        return this.side.equals(OrderSide.ASK) ? OrderSide.BID : OrderSide.ASK;
    }

    @Data
    public static class Level {
        private long price;
        private long totalSize;

        Level(long price, long totalSize) {
            this.price = price;
            this.totalSize = 0;
        }

        @Override
        protected Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                return new Level(this.price, this.totalSize);
            }

        }
    }

}
