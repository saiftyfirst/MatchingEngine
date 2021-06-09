package matching.engine.core.common.order;

public interface IOrder {

    long getInstrument();

    double getPrice();

    double getSize();

    OrderSide getSide();

    long getOrderId();

    long getUid();

    double getFilled();


    long getTimestamp();

}
