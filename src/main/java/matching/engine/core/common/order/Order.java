package matching.engine.core.common.order;

import lombok.Builder;
import lombok.Data;
import matching.engine.core.api.OrderRequest;

@Data
@Builder
public class Order implements IOrder {

    private long instrument;

    private long price;

    private long size;

    private OrderSide side;

    private long userId;

    private long orderId;

    private long remainingSize;

    private long timestamp;

    public void reduceSize(long reducible) {
        this.remainingSize -= reducible;
    }

    public static Order orderFromReq(OrderRequest request) {
        return new Order(
                request.getInstrument(),
                request.getPrice(),
                request.getSize(),
                request.getSide(),
                request.getUserId(),
                request.getOrderId(),
                request.getSize(),
                request.getTimestamp()
                );
    }

}
