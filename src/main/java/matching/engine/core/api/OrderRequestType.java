package matching.engine.core.api;

public enum OrderRequestType {

    NEW_ORDER(0), CANCEL_ORDER(1), AMEND_PRICE(2), AMEND_SIZE(3);

    private final int code;

    OrderRequestType(int code) {
        this.code = code;
    }

    int getCode() {
        return code;
    }

    public static OrderRequestType of(int code) {
        switch (code) {
            case 0:
                return NEW_ORDER;
            case 1:
                return CANCEL_ORDER;
            case 2:
                return AMEND_PRICE;
            case 3:
                return AMEND_SIZE;
            default:
                throw new IllegalArgumentException("unknown OrderRequest: " + code);
        }
    }






}
