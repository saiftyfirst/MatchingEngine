package matching.engine.core.orderbook.events;

public enum FillType {

    FULL(0),
    PARTIAL(1);

    private final int code;

    FillType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static FillType of(int code) {
        switch (code) {
            case 1:
                return FULL;
            case 2:
                return PARTIAL;
            default:
                throw new IllegalArgumentException("unknown FillType: " + code);
        }
    }
}
