package matching.engine.core.api;

import lombok.Builder;
import lombok.Getter;
import matching.engine.core.common.order.IOrder;
import matching.engine.core.common.order.OrderSide;
import matching.engine.core.common.order.OrderSpec;

@Builder
public class OrderRequest implements IOrder {

    @Getter
    final private double price;

    @Getter
    final private double size;

    @Getter
    final private OrderSide side;

    @Getter
    final private Long uid;

    @Getter
    final private long orderId;

    @Getter
    final private double filled;

    @Getter
    final private long timestamp;

    @Getter
    final private OrderRequestType orderRequestType;

    @Getter
    final private OrderSpec orderSpec;

}
