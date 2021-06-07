package matching.engine.core.common.trade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import matching.engine.core.orderbook.events.FillType;

@Getter
@AllArgsConstructor
public class Trade {

    private final double price;

    private final double size;

    private final long buyerUid;

    private final long buyerOrderId;

    private final FillType buyerFill;

    private final long sellerUId;

    private final long sellerOrderId;

    private final FillType sellerFill;

    private final long timestamp;

}
