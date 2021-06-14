package matching.engine.core.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import matching.engine.core.common.OrderRequest;

@Data
@AllArgsConstructor
public class Order implements IOrder {

    private long price;

    private long size;

    private Side side;

    private long orderId;

    private long userId;

    private long instrument;

    private long remainingSize;

    public static Order orderFromOrderRequest(OrderRequest request) {
        OrderRequest.RequestType requestType = request.getRequestType();
        Order order = null;
        if (!isModifier(request)) {
            order = new Order(request.getPrice(), request.getSize(),
                    request.getSide(), request.getOrderId(),
                    request.getUserId(), request.getInstrument(), request.getSize());
        }
        return order;
    }

    private static boolean isModifier(OrderRequest request) {
        OrderRequest.RequestType requestType = request.getRequestType();
        return requestType.equals(OrderRequest.RequestType.AMEND_SIZE)
                || requestType.equals(OrderRequest.RequestType.CANCEL_ORDER)
                || requestType.equals(OrderRequest.RequestType.AMEND_PRICE);
    }


}

