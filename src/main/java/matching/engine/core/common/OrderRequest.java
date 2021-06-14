package matching.engine.core.common;

import lombok.Builder;
import lombok.Data;
import matching.engine.core.order.IOrder;
import matching.engine.core.order.Side;

@Data
@Builder
public class OrderRequest {

    private long price;

    private long size;

    private Side side;

    private long userId;

    private long orderId;

    private RequestType requestType;

    private long timestamp;

    private long instrument;

    public static enum RequestType {
        MARKER_ORDER, LIMIT_ORDER, FOK_ORDER, AMEND_PRICE, AMEND_SIZE, CANCEL_ORDER
    }

}
