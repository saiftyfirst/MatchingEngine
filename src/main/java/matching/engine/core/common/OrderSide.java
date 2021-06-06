package matching.engine.core.common;

public enum OrderSide {

    BID(0), ASK(1);

    private final int code;

    OrderSide(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OrderSide of(int code) {
        switch (code) {
            case 0:
                return BID;
            case 1:
                return ASK;
            default:
                throw new IllegalArgumentException("unknown OrderSide: " + code);
        }
    }

}
