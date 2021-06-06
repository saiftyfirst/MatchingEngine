package matching.engine.core.common;

public interface IOrder {

    double getPrice();

    double getSize();

    OrderSide getSide();

    long getOrderId();

    long getUserId();

    double getFilled();

    long getTimestamp();

}
