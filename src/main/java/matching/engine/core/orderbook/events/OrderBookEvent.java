package matching.engine.core.orderbook.events;

import lombok.Data;
import matching.engine.core.api.OrderRequest;
import matching.engine.core.common.trade.Trade;

import java.util.Collection;

public interface OrderBookEvent {

    @Data
    class Match {
        Collection<Trade> trades;
    }

    @Data
    class MatchWithReject {
        Collection<Trade> trades;
        Reject reject;
    }

    @Data
    class Reject {
        OrderRequest orderRequest;
        RejectReason rejectReason;
    }


}
