package matching.engine.core.common.order;

public enum OrderSpec {

    LIMIT(0), // equivalent to limit order
    MARKET(1), // equivalent to market order
    FOK(2); // fill as much as possible and cancel the rest

    private final int code;

    OrderSpec(int code) {
        this.code = code;
    }

    public static OrderSpec of(int code) {
        switch (code) {
            case 0:
                return LIMIT;
            case 1:
                return MARKET;
            case 2:
                return FOK;
            default:
                throw new IllegalArgumentException("No OrderSpec with code: " + code);
        }
    }


}
