package kata.himnabil.sales.analytics;

/**
 * Represents a refund event for an order.
 * <p>
 * This record implements the {@link OrderEvent} interface and represents
 * a refund for an order identified by its order ID, with a specific refund amount.
 */
public record Refund(
        /**
         * The unique identifier of the order being refunded.
         */
        long orderId,

        /**
         * The amount being refunded for the order.
         */
        double amount) implements OrderEvent {}
