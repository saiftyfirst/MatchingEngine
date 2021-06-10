package matching.engine.core.orderbook;

import matching.engine.core.common.order.Order;
import matching.engine.core.common.order.OrderSide;
import matching.engine.core.common.trade.Trade;
import matching.engine.core.orderbook.events.FillType;

import java.util.*;

class OrderBookEntity {

    private long price;
    private long totalSize;
    private OrderSide side;
    private final LinkedHashMap<Long, Order> orderChain;

    OrderBookEntity(long price, OrderSide side) {
        this.price = price;
        this.totalSize = 0;
        this.side = side;
        orderChain = new LinkedHashMap<>();
    }

    public long getPrice() {
        return price;
    }

    long getTotalSize() {
        return totalSize;
    }

    void addOrder(Order order) {
        this.totalSize += order.getSize();
        this.orderChain.put(order.getOrderId(), order);
    }

    boolean removeOrder(long orderId) {
        if (this.orderChain.containsKey(orderId)) {
            Order removable = this.orderChain.remove(orderId);
            this.totalSize -= removable.getSize();
            return true;
        }
        return false;
    }

    boolean reduceSize(long orderId, long newSize) {
        if (this.orderChain.containsKey(orderId)) {
            Order reducible = this.orderChain.remove(orderId);
            this.totalSize -= reducible.getSize();
            reducible.setSize(newSize);
            this.totalSize += newSize;
            return true;
        }
        return false;
    }

    List<Trade> matchOrders(Order matchable) {

        // TODO: ONLY WORKS FOR LIMIT ORDERS

        List<Trade> trades = new ArrayList<>();
        if (this.side.equals(getOppositeSide())) {

            long matchablePrice = matchable.getPrice();
            long toFill = matchable.getRemainingSize();

            List<Long> ordersToRemove = new ArrayList<>();

            long tradeSize;
            if (this.side.equals(OrderSide.ASK)) {
                if (matchablePrice < this.price) {
                    return trades;
                }

                for (Map.Entry<Long, Order> entry: this.orderChain.entrySet()) {
                    if (toFill == 0) break;

                    tradeSize = Math.min(entry.getValue().getSize(), toFill);
                    entry.getValue().reduceSize(tradeSize);
                    toFill -= tradeSize;

                    trades.add(new Trade(
                            price,
                            tradeSize,
                            matchable.getUid(),
                            matchable.getOrderId(),
                            tradeSize == matchable.getSize() ? FillType.FULL : FillType.PARTIAL,
                            toFill,
                            entry.getValue().getUid(),
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
                if (matchablePrice > this.price) {
                    return Collections.emptyList();
                }

                for (Map.Entry<Long, Order> entry: this.orderChain.entrySet()) {
                    if (toFill == 0) break;

                    tradeSize = Math.min(entry.getValue().getSize(), toFill);
                    entry.getValue().reduceSize(tradeSize);
                    toFill -= tradeSize;

                    trades.add(new Trade(
                            price,
                            tradeSize,
                            entry.getValue().getUid(),
                            entry.getValue().getOrderId(),
                            entry.getValue().getRemainingSize() == 0 ? FillType.FULL : FillType.PARTIAL,
                            entry.getValue().getRemainingSize(),
                            matchable.getUid(),
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

    private OrderSide getOppositeSide() {
        return this.side.equals(OrderSide.ASK) ? OrderSide.BID : OrderSide.ASK;
    }

}
