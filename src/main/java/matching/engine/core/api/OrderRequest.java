package matching.engine.core.api;

import lombok.Builder;
import lombok.Getter;
import matching.engine.core.common.IOrder;
import matching.engine.core.common.OrderSide;

@Builder
public class OrderRequest implements IOrder {

    @Getter
    private double price;

    @Getter
    private double size;

    @Getter
    private OrderSide side;

    @Getter
    private Long userId;

    @Getter
    private long orderId;

    @Getter
    private double filled;

    @Getter
    private long timestamp;

    @Getter
    private OrderRequestType orderRequestType;

}
