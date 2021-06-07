package matching.engine.core.common.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order implements IOrder {

    private double price;

    private double size;

    private OrderSide side;

    private Long uid;

    private long orderId;

    private double filled;

    private long timestamp;

}
