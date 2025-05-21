package kata.himnabil.sales.analytics;

/**
 * Represents a cancellation event for an order.
 * <p>
 * This record implements the {@link OrderEvent} interface and represents
 * the cancellation of an order identified by its order ID.
 */
public record Cancel(
        /**
         * The unique identifier of the order being cancelled.
         */
        long orderId) implements OrderEvent {}
