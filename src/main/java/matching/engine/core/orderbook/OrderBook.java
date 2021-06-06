package matching.engine.core.orderbook;

import matching.engine.core.api.OrderRequest;
import matching.engine.core.api.OrderResponse;
import matching.engine.core.common.IOrder;

import java.util.Collection;

public class OrderBook implements IOrderBook {


    @Override
    public OrderResponse newOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public OrderResponse amendOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public OrderResponse cancelOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public Collection<IOrder> getBids() {
        return null;
    }

    @Override
    public Collection<IOrder> getAsks() {
        return null;
    }

    @Override
    public long getSymbol() {
        return 0;
    }

}
