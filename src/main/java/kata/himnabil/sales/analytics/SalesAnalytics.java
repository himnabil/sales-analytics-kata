package kata.himnabil.sales.analytics;

import java.util.*;

/**
 * Interface for analyzing sales data from various order events.
 * Provides methods to track and analyze sales metrics across different channels.
 */
public interface SalesAnalytics {

    /**
     * Factory method to create a new instance of SalesAnalytics.
     *
     * @return a new SalesAnalytics instance
     */
    static SalesAnalytics create() {
        // TODO: create your implementation here
        return null;
    }

    /**
     * Adds an order event to the analytics system.
     * This operation should have O(1) time complexity.
     *
     * @param event the order event to add (can be a new order, cancellation, or refund)
     */
    void add(OrderEvent event);               // O(1)

    /**
     * Calculates the total net revenue across all channels.
     * Net revenue is calculated as the total order amount minus refunds.
     *
     * @return the total net revenue as a double
     */
    double totalNetRevenue();                 // revenue – refunds

    /**
     * Calculates the net revenue broken down by sales channel.
     *
     * @return a map with Channel as key and net revenue as value
     */
    Map<Channel, Double> netRevenueByChannel();

    /**
     * Returns the top N customers by their net spend.
     * Net spend is calculated after accounting for refunds and cancellations.
     *
     * @param n the number of top customers to return
     * @return a list of customer IDs sorted by spend in descending order
     */
    List<String> topNCustomersBySpend(int n); // after refunds/cancels

    /**
     * Calculates the average order value for a specific channel.
     *
     * @param c the channel to calculate the average order value for
     * @return an OptionalDouble containing the average order value, or empty if no orders exist for the channel
     */
    OptionalDouble averageOrderValue(Channel c);
}
