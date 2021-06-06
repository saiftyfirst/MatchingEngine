package matching.engine.core.common;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Order implements IOrder {

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

}
