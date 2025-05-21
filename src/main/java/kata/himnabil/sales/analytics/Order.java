package kata.himnabil.sales.analytics;

/**
 * Represents an order in the sales system.
 * <p>
 * This record contains all the details of an order, including its unique identifier,
 * the channel through which it was placed, the customer who placed it, and the order amount.
 */
public record Order(
        /**
         * The unique identifier for the order.
         */
        String id,

        /**
         * The sales channel through which the order was placed.
         */
        Channel channel,

        /**
         * The unique identifier of the customer who placed the order.
         */
        String customerId,

        /**
         * The monetary amount of the order.
         */
        double amount
) {}
