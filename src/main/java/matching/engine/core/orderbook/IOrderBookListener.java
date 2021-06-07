package matching.engine.core.orderbook;

import matching.engine.core.orderbook.events.OrderBookEvent;

public interface IOrderBookListener {

    void process(OrderBookEvent orderBookEvent);

}
