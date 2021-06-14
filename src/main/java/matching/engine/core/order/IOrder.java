package matching.engine.core.order;

public interface IOrder {

    long getPrice();

    long getSize();

    Side getSide();

    long getOrderId();

    long getUserId();

    long getInstrument();

    long getRemainingSize();

}
