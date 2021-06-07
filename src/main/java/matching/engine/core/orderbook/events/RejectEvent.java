package matching.engine.core.orderbook.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matching.engine.core.api.OrderRequest;

@Data
@AllArgsConstructor
public class RejectEvent {

    OrderRequest orderRequest;

    RejectReason rejectReason;

    double unFilledSize;

    public static enum RejectReason {

        BAD_SYMBOL(0),
        DUPLICATE_ORDER(1),
        UNFILLED(2);

        private int code;

        RejectReason(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static RejectReason of(int code) {
            switch (code) {
                case 1:
                    return BAD_SYMBOL;
                case 2:
                    return DUPLICATE_ORDER;
                case 3:
                    return UNFILLED;
                default:
                    throw new IllegalArgumentException("unknown RejectReason: " + code);
            }
        }
    }


}
