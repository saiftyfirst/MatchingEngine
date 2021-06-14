package matching.engine.core.orderbook;

import matching.engine.core.common.OrderRequest;
import matching.engine.core.order.Order;

import java.util.*;
import java.util.stream.Collectors;

public class OrderBook<OrderChain extends AbstractOrderChain> implements IOrderBook {

    private NavigableMap<Long, OrderChain> bids;
    private NavigableMap<Long, OrderChain> asks;


    public OrderBook() {
        this.bids = new TreeMap<>(Collections.reverseOrder());
        this.asks = new TreeMap<>();
    }

    @Override
    public void marketOrder(OrderRequest request) {

    }

    @Override
    public void newLimitOrder(OrderRequest request) {

    }

    @Override
    public void newFOKOrder(OrderRequest request) {

    }

    @Override
    public void amendOrderSize(OrderRequest request) {

    }

    @Override
    public void amendOrderPrice(OrderRequest request) {

    }

    @Override
    public void cancelOrder(OrderRequest request) {

    }

    @Override
    public List<Level> getBids() {
        return this.bids.values().stream().map(AbstractOrderChain::getLevel).collect(Collectors.toList());
    }

    @Override
    public List<Level> getAsks() {
        return this.asks.values().stream().map(AbstractOrderChain::getLevel).collect(Collectors.toList());
    }

    @Override
    public List<Order> getUserOrders() {
        return null;
    }
}
