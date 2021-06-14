package matching.engine.core.trade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import matching.engine.core.order.Side;

@Getter
@AllArgsConstructor
public class Trade {

    private final long price;

    private final long size;

    private final long takerOrderId;

    private final long takerId;

    private final Side takerSide;

    private final FillType takerFillType;

    private final long makerOrderId;

    private final long makerId;

    private final Side makerSide;

    private final FillType makerFillType;

    private final long timestamp;

}
