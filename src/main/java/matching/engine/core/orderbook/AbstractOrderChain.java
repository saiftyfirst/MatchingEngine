package matching.engine.core.orderbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import matching.engine.core.order.Order;
import matching.engine.core.trade.Trade;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class AbstractOrderChain {

    protected final long price;

    protected long totalSize;

    public Level getLevel() {
        return new Level(price, totalSize);
    }

    public abstract void chainOrder(Order chainable);

    public abstract boolean removeOrder(long orderId);

    public abstract boolean amendOrderSize(long orderId, long newSize);

    public abstract List<Trade> getMatches(Order matchable);

}
