package kata.himnabil.sales.analytics;

/**
 * Represents an event related to an order in the sales system.
 * <p>
 * This is a sealed interface that can only be implemented by the specified
 * permitted classes: {@link Cancel}, {@link NewOrder}, and {@link Refund}.
 * Each implementation represents a different type of order event that can
 * occur in the system.
 */
public sealed interface OrderEvent permits Cancel, NewOrder, Refund {

}
