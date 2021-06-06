package matching.engine.core.orderbook;

import matching.engine.core.api.OrderRequest;
import matching.engine.core.api.OrderResponse;
import matching.engine.core.common.IOrder;

import java.util.Collection;

public interface IOrderBook {

    OrderResponse newOrder(OrderRequest orderRequest);

    OrderResponse amendOrder(OrderRequest orderRequest);

    OrderResponse cancelOrder(OrderRequest orderRequest);

    Collection<IOrder> getBids();

    Collection<IOrder> getAsks();

    long getSymbol();

}
