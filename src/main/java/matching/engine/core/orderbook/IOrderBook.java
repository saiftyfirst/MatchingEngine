package matching.engine.core.orderbook;

import matching.engine.core.api.OrderRequest;
import matching.engine.core.common.order.IOrder;

import java.util.Collection;

public interface IOrderBook {

    void newOrder(OrderRequest orderRequest);

    void amendOrder(OrderRequest orderRequest);

    void cancelOrder(OrderRequest orderRequest);

    Collection<IOrder> getBids();

    Collection<IOrder> getAsks();

    long getInstrument();

    Collection<IOrder> getUserOrders(long userId);

    void addListener(IOrderBookListener orderBookListener);

    void removeListener(IOrderBookListener orderBookListener);

    void removeAllListeners();

}
