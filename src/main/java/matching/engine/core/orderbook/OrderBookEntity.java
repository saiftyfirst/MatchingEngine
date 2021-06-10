package matching.engine.core.orderbook;

import matching.engine.core.common.order.Order;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

class OrderBookEntity {

    private double price;
    private LinkedHashMap<Long, Order> orderChain = new LinkedHashMap<>();



    OrderBookEntity() {

    }





}
