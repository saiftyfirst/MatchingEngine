package matching.engine.core.orderbook.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import matching.engine.core.api.OrderRequest;
import matching.engine.core.common.order.Order;
import matching.engine.core.common.trade.Trade;

import java.util.Collection;

public interface OrderBookEvent {

    @Data
    @AllArgsConstructor
    class Match implements OrderBookEvent {
        Collection<Trade> trades;
    }

    @Data
    @AllArgsConstructor
    class MatchWithReject implements OrderBookEvent {
        Collection<Trade> trades;
        Reject reject;
    }

    @Data
    @AllArgsConstructor
    class Reject implements OrderBookEvent {
        OrderRequest orderRequest;
        RejectReason rejectReason;
    }


}
