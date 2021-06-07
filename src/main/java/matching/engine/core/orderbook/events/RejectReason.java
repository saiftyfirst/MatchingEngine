package matching.engine.core.orderbook.events;

public enum RejectReason {

    BAD_SYMBOL(0),
    DUPLICATE_ORDER(1),
    ORDER_NOT_FOUND(2),
    PARTIAL_FOK(3);

    private final int code;

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
                return ORDER_NOT_FOUND;
            case 4:
                return PARTIAL_FOK;
            default:
                throw new IllegalArgumentException("unknown RejectReason: " + code);
        }
    }
}