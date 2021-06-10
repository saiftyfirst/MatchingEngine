package matching.engine.core.api;

import lombok.Builder;
import lombok.Getter;
import matching.engine.core.common.order.IOrder;
import matching.engine.core.common.order.OrderSide;
import matching.engine.core.common.order.OrderType;

@Builder
public class OrderRequest implements IOrder {

    @Getter
    final private long price;

    @Getter
    final private long size;

    @Getter
    final private OrderSide side;

    @Getter
    final private Long uid;

    @Getter
    final private long orderId;

    @Getter
    final private long remainingSize;

    @Getter
    final private long timestamp;

    @Getter
    final private OrderRequestType orderRequestType;

    @Getter
    final private OrderType orderType;

    @Getter
    final private long instrument;

}
