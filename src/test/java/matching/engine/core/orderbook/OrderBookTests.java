package matching.engine.core.orderbook;

import matching.engine.core.api.OrderRequest;
import matching.engine.core.api.OrderRequestType;
import matching.engine.core.common.order.OrderSide;
import matching.engine.core.common.order.OrderType;
import matching.engine.core.orderbook.events.OrderBookEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class OrderBookTests {

    private IOrderBook orderBook;
    private long instrument;

    private MutableListener mutableListener;


    @Before
    public void setup() {
        this.instrument = 199L;
        this.orderBook = new OrderBook(this.instrument);
        this.mutableListener = new MutableListener(this.orderBook);

    }

    @Test
    public void orderBookStoresAsks() {
        OrderRequest orderRequest = new OrderRequest(12L, 10L, OrderSide.ASK, 7L, 10L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);

        this.orderBook.newOrder(orderRequest);

        List<OrderBookEntity.Level> expectedAsks = Collections.singletonList(
                new OrderBookEntity.Level(12L, 10L)
        );

        List<OrderBookEntity.Level> expectedBids = Collections.emptyList();

        Assert.assertTrue(orderBookStateCompare(
                this.orderBook.getBids(), this.orderBook.getAsks(),
                expectedBids, expectedAsks
                )
        );
    }

    @Test
    public void orderBookChainsAsks() {
        OrderRequest orderRequest1 = new OrderRequest(12L, 10L, OrderSide.ASK, 7L, 10L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        OrderRequest orderRequest2 = new OrderRequest(12L, 10L, OrderSide.ASK, 7L, 11L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        this.orderBook.newOrder(orderRequest1);
        this.orderBook.newOrder(orderRequest2);

        List<OrderBookEntity.Level> expectedAsks = Collections.singletonList(
                new OrderBookEntity.Level(12L, 20L)
        );

        List<OrderBookEntity.Level> expectedBids = Collections.emptyList();

        Assert.assertTrue(orderBookStateCompare(
                this.orderBook.getBids(), this.orderBook.getAsks(),
                expectedBids, expectedAsks
                )
        );

    }

    @Test
    public void orderBookOrdersAsks() {
        OrderRequest orderRequest1 = new OrderRequest(12L, 10L, OrderSide.ASK, 7L, 10L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        OrderRequest orderRequest2 = new OrderRequest(13L, 10L, OrderSide.ASK, 7L, 11L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        this.orderBook.newOrder(orderRequest1);
        this.orderBook.newOrder(orderRequest2);

        List<OrderBookEntity.Level> expectedAsks = Arrays.asList(
                new OrderBookEntity.Level(12L, 10L),
                new OrderBookEntity.Level(13L, 10L)
                );

        List<OrderBookEntity.Level> expectedBids = Collections.emptyList();

        Assert.assertTrue(orderBookStateCompare(
                this.orderBook.getBids(), this.orderBook.getAsks(),
                expectedBids, expectedAsks
                )
        );
    }

    @Test
    public void orderBookStoresBids() {
        OrderRequest orderRequest = new OrderRequest(12L, 10L, OrderSide.BID, 7L, 10L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);

        this.orderBook.newOrder(orderRequest);

        List<OrderBookEntity.Level> expectedBids = Collections.singletonList(
                new OrderBookEntity.Level(12L, 10L)
        );

        List<OrderBookEntity.Level> expectedAsks = Collections.emptyList();

        Assert.assertTrue(orderBookStateCompare(
                this.orderBook.getBids(), this.orderBook.getAsks(),
                expectedBids, expectedAsks
                )
        );
    }

    @Test
    public void orderBookChainsBids() {
        OrderRequest orderRequest1 = new OrderRequest(12L, 10L, OrderSide.BID, 7L, 10L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        OrderRequest orderRequest2 = new OrderRequest(12L, 10L, OrderSide.BID, 7L, 11L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        this.orderBook.newOrder(orderRequest1);
        this.orderBook.newOrder(orderRequest2);

        List<OrderBookEntity.Level> expectedBids = Collections.singletonList(
                new OrderBookEntity.Level(12L, 10L)
        );

        List<OrderBookEntity.Level> expectedAsks = Collections.emptyList();

        Assert.assertTrue(orderBookStateCompare(
                this.orderBook.getBids(), this.orderBook.getAsks(),
                expectedBids, expectedAsks
                )
        );
    }

    @Test
    public void orderBookOrdersBids() {
        OrderRequest orderRequest1 = new OrderRequest(12L, 10L, OrderSide.BID, 7L, 10L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        OrderRequest orderRequest2 = new OrderRequest(13L, 10L, OrderSide.BID, 7L, 11L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);

        this.orderBook.newOrder(orderRequest1);
        this.orderBook.newOrder(orderRequest2);

        List<OrderBookEntity.Level> expectedBids = Arrays.asList(
                new OrderBookEntity.Level(13L, 10L),
                new OrderBookEntity.Level(12L, 10L)
                );

        List<OrderBookEntity.Level> expectedAsks = Collections.emptyList();

        Assert.assertTrue(orderBookStateCompare(
                this.orderBook.getBids(), this.orderBook.getAsks(),
                expectedBids, expectedAsks
                )
        );
    }

    @Test
    public void orderBookMatchesLimitOrderInstantly() {
        OrderRequest orderRequest1 = new OrderRequest(12L, 10L, OrderSide.BID, 7L, 10L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);

        OrderRequest orderRequest3 = new OrderRequest(12L, 5L, OrderSide.ASK, 7L, 11L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);

        this.orderBook.newOrder(orderRequest1);
        this.orderBook.newOrder(orderRequest3);

        List<OrderBookEntity.Level> expectedBids = Collections.singletonList(
                new OrderBookEntity.Level(12L, 5L)
        );

        List<OrderBookEntity.Level> expectedAsks = Collections.emptyList();

        Assert.assertTrue(orderBookStateCompare(
                this.orderBook.getBids(), this.orderBook.getAsks(),
                expectedBids, expectedAsks
                )
        );

    }

    @Test
    public void orderBookMatchesOrderSequentially() {
        OrderRequest orderRequest1 = new OrderRequest(12L, 10L, OrderSide.BID, 7L, 10L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        OrderRequest orderRequest2 = new OrderRequest(12L, 5L, OrderSide.BID, 7L, 12L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);
        OrderRequest orderRequest3 = new OrderRequest(12L, 12L, OrderSide.ASK, 7L, 14L,
                10L, 1L, OrderRequestType.NEW_ORDER, OrderType.LIMIT, this.instrument);

        this.orderBook.newOrder(orderRequest1);
        this.orderBook.newOrder(orderRequest2);
        this.orderBook.newOrder(orderRequest3);

        List<OrderBookEntity.Level> expectedBids = Collections.singletonList(
                new OrderBookEntity.Level(12L, 3L)
        );

        List<OrderBookEntity.Level> expectedAsks = Collections.emptyList();

        Assert.assertTrue(orderBookStateCompare(
                this.orderBook.getBids(), this.orderBook.getAsks(),
                expectedBids, expectedAsks
                )
        );

    }

    private static class MutableListener implements IOrderBookListener {

        Consumer<OrderBookEvent> consumer;

        MutableListener(IOrderBook orderBook) {
            orderBook.addListener(this);
        }

        public void setRunnable(Consumer<OrderBookEvent> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void process(OrderBookEvent orderBookEvent) {
            if (consumer != null) {
                consumer.accept(orderBookEvent);
            }
        }
    }

    private boolean orderBookStateCompare(List<OrderBookEntity.Level> bids, List<OrderBookEntity.Level> asks,
                                          List<OrderBookEntity.Level> expectedBids, List<OrderBookEntity.Level> expectedAsks) {

        if (bids.size() != expectedBids.size() || asks.size() != expectedAsks.size()) {
            return false;
        }

        OrderBookEntity.Level actual;
        OrderBookEntity.Level expected;
        for (int i = 0; i < bids.size(); i++) {
            actual = bids.get(i);
            expected = expectedBids.get(i);
            if (actual.getPrice() != expected.getPrice() || actual.getTotalSize() != expected.getTotalSize()) {
                return false;
            }
        }
        for (int i = 0; i < asks.size(); i++) {
            actual = asks.get(i);
            expected = expectedAsks.get(i);
            if (actual.getPrice() != expected.getPrice() || actual.getTotalSize() != expected.getTotalSize()) {
                return false;
            }
        }

        return true;
    }


}
