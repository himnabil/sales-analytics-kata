package kata.himnabil.sales.analytics;

/**
 * Represents a new order event in the sales system.
 * <p>
 * This record implements the {@link OrderEvent} interface and represents
 * the creation of a new order in the system.
 */
public record NewOrder(
        /**
         * The order details for the new order.
         */
        Order order) implements OrderEvent {}
