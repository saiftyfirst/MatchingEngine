package matching.engine.core.orderbook;

import matching.engine.core.common.OrderRequest;
import matching.engine.core.order.Order;

import java.util.List;

public interface IOrderBook {

    void marketOrder(OrderRequest request);

    void newLimitOrder(OrderRequest request);

    void newFOKOrder(OrderRequest request);

    void amendOrderSize(OrderRequest request);

    void amendOrderPrice(OrderRequest request);

    void cancelOrder(OrderRequest request);

    List<Level> getBids();

    List<Level> getAsks();

    List<Order> getUserOrders();

}
