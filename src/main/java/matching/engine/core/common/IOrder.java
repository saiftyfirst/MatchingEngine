package matching.engine.core.common;

public interface IOrder {

    long getPrice();

    long getSize();

    OrderSide getSide();

    long getOrderId();

    long getUserId();

    long getFilled();

    long getTimestamp();

}
