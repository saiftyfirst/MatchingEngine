package matching.engine.core.common.order;

public interface IOrder {

    long getInstrument();

    long getPrice();

    long getSize();

    OrderSide getSide();

    long getOrderId();

    long getUserId();

    long getRemainingSize();

    long getTimestamp();

}
