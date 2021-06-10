package matching.engine.core.common.trade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import matching.engine.core.orderbook.events.FillType;

@Getter
@AllArgsConstructor
public class Trade {

    private final long price;

    private final long size;

    private final long buyerUid;

    private final long buyerOrderId;

    private final FillType buyerFill;

    private final long buyerRemainingSize;

    private final long sellerUId;

    private final long sellerOrderId;

    private final FillType sellerFill;

    private final long sellerRemainingSize;

    private final long timestamp;

}
