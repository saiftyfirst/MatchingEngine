package matching.engine.core.orderbook;

import matching.engine.core.api.OrderRequest;
import matching.engine.core.common.order.IOrder;

import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;

public class OrderBook implements IOrderBook {

    private final long instrument;
    private NavigableMap<Double, OrderBookEntity> bids;
    private NavigableMap<Double, OrderBookEntity> asks;

    public OrderBook(long instrument) {
        this.instrument = instrument;
    }


    @Override
    public void newOrder(OrderRequest orderRequest) {
        /* Possible Events:
         * case LIMIT:
         *  - reject due to bad instrument
         *  - reject due to duplicate order id
         *  - full fill and return one or more trades
         *  - partial fill and return one or more trades

         * case MARKET:
         *  - reject due to bad instrument
         *  - reject due to duplicate order id
         *  - full fill and return one or more trades

         * case FOK:
         *  - reject due to bad instrument
         *  - reject due to duplicate order id
         *  - full fill and return one or more trades
         *  - partial fill and return one or more trades, reject remaining size
         * */
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

}
