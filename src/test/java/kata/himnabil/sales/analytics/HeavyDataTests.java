package kata.himnabil.sales.analytics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests with large datasets for the SalesAnalytics interface.
 */
@DisplayName("Heavy data tests")
class HeavyDataTests extends BaseSalesAnalyticsTest {
    private static final int LARGE_DATASET_SIZE = 1000;
    private static final int MEDIUM_DATASET_SIZE = 500;
    private static final int SMALL_DATASET_SIZE = 100;

    @Test
    @DisplayName("Should calculate total net revenue with large number of orders")
    void shouldCalculateTotalNetRevenueWithLargeNumberOfOrders() {
        // Given
        double expectedTotalRevenue = 0.0;
        for (int i = 1; i <= LARGE_DATASET_SIZE; i++) {
            double amount = i * 10.0;
            expectedTotalRevenue += amount;
            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i),
                Channel.values()[i % Channel.values().length],
                "customer" + (i % 100),
                amount
            )));
        }

        // When
        double totalRevenue = salesAnalytics.totalNetRevenue();

        // Then
        assertThat(totalRevenue).isEqualTo(expectedTotalRevenue);
    }

    @Test
    @DisplayName("Should calculate total net revenue with large number of orders, refunds and cancellations")
    void shouldCalculateTotalNetRevenueWithLargeNumberOfOrdersRefundsAndCancellations() {
        // Given
        double expectedTotalRevenue = 0.0;

        // Add orders
        for (int i = 1; i <= LARGE_DATASET_SIZE; i++) {
            double amount = i * 10.0;
            expectedTotalRevenue += amount;
            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i),
                Channel.values()[i % Channel.values().length],
                "customer" + (i % 100),
                amount
            )));
        }

        // Cancel 20% of orders
        for (int i = 1; i <= LARGE_DATASET_SIZE; i += 5) {
            salesAnalytics.add(new Cancel(i));
            expectedTotalRevenue -= i * 10.0;
        }

        // Refund 10% of orders (partial refunds)
        for (int i = 2; i <= LARGE_DATASET_SIZE; i += 10) {
            double refundAmount = i * 5.0; // 50% refund
            salesAnalytics.add(new Refund(i, refundAmount));
            expectedTotalRevenue -= refundAmount;
        }

        // When
        double totalRevenue = salesAnalytics.totalNetRevenue();

        // Then
        assertThat(totalRevenue).isEqualTo(expectedTotalRevenue);
    }

    @Test
    @DisplayName("Should calculate revenue by channel with large number of orders")
    void shouldCalculateRevenueByChannelWithLargeNumberOfOrders() {
        // Given
        Map<Channel, Double> expectedRevenueByChannel = new HashMap<>();
        for (Channel channel : Channel.values()) {
            expectedRevenueByChannel.put(channel, 0.0);
        }

        for (int i = 1; i <= LARGE_DATASET_SIZE; i++) {
            Channel channel = Channel.values()[i % Channel.values().length];
            double amount = i * 10.0;
            expectedRevenueByChannel.put(channel, expectedRevenueByChannel.get(channel) + amount);

            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i),
                channel,
                "customer" + (i % 100),
                amount
            )));
        }

        // When
        Map<Channel, Double> revenueByChannel = salesAnalytics.netRevenueByChannel();

        // Then
        assertThat(revenueByChannel).containsExactlyInAnyOrderEntriesOf(expectedRevenueByChannel);
    }

    @Test
    @DisplayName("Should calculate revenue by channel with large number of orders, refunds and cancellations")
    void shouldCalculateRevenueByChannelWithLargeNumberOfOrdersRefundsAndCancellations() {
        // Given
        Map<Channel, Double> expectedRevenueByChannel = new HashMap<>();
        Map<Integer, Channel> orderChannels = new HashMap<>();

        for (Channel channel : Channel.values()) {
            expectedRevenueByChannel.put(channel, 0.0);
        }

        // Add orders
        for (int i = 1; i <= LARGE_DATASET_SIZE; i++) {
            Channel channel = Channel.values()[i % Channel.values().length];
            orderChannels.put(i, channel);
            double amount = i * 10.0;
            expectedRevenueByChannel.put(channel, expectedRevenueByChannel.get(channel) + amount);

            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i),
                channel,
                "customer" + (i % 100),
                amount
            )));
        }

        // Cancel 20% of orders
        for (int i = 1; i <= LARGE_DATASET_SIZE; i += 5) {
            Channel channel = orderChannels.get(i);
            double amount = i * 10.0;
            expectedRevenueByChannel.put(channel, expectedRevenueByChannel.get(channel) - amount);
            salesAnalytics.add(new Cancel(i));
        }

        // Refund 10% of orders (partial refunds)
        for (int i = 2; i <= LARGE_DATASET_SIZE; i += 10) {
            Channel channel = orderChannels.get(i);
            double refundAmount = i * 5.0; // 50% refund
            expectedRevenueByChannel.put(channel, expectedRevenueByChannel.get(channel) - refundAmount);
            salesAnalytics.add(new Refund(i, refundAmount));
        }

        // When
        Map<Channel, Double> revenueByChannel = salesAnalytics.netRevenueByChannel();

        // Then
        assertThat(revenueByChannel).containsExactlyInAnyOrderEntriesOf(expectedRevenueByChannel);
    }

    @Test
    @DisplayName("Should find top N customers with large number of orders")
    void shouldFindTopNCustomersWithLargeNumberOfOrders() {
        // Given
        Map<String, Double> customerSpends = new HashMap<>();

        // Add orders for many customers with varying spend amounts
        for (int i = 1; i <= LARGE_DATASET_SIZE; i++) {
            String customerId = "customer" + (i % 100); // 100 different customers
            double amount = i * 10.0;

            customerSpends.put(customerId, customerSpends.getOrDefault(customerId, 0.0) + amount);

            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i),
                Channel.values()[i % Channel.values().length],
                customerId,
                amount
            )));
        }

        // Get expected top 10 customers
        List<String> expectedTopCustomers = customerSpends.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // When
        List<String> topCustomers = salesAnalytics.topNCustomersBySpend(10);

        // Then
        assertThat(topCustomers).containsExactlyElementsOf(expectedTopCustomers);
    }

    @Test
    @DisplayName("Should find top N customers with large number of orders, refunds and cancellations")
    void shouldFindTopNCustomersWithLargeNumberOfOrdersRefundsAndCancellations() {
        // Given
        Map<String, Double> customerSpends = new HashMap<>();
        Map<Integer, String> orderCustomers = new HashMap<>();

        // Add orders for many customers with varying spend amounts
        for (int i = 1; i <= LARGE_DATASET_SIZE; i++) {
            String customerId = "customer" + (i % 100); // 100 different customers
            orderCustomers.put(i, customerId);
            double amount = i * 10.0;

            customerSpends.put(customerId, customerSpends.getOrDefault(customerId, 0.0) + amount);

            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i),
                Channel.values()[i % Channel.values().length],
                customerId,
                amount
            )));
        }

        // Cancel 20% of orders
        for (int i = 1; i <= LARGE_DATASET_SIZE; i += 5) {
            String customerId = orderCustomers.get(i);
            double amount = i * 10.0;
            customerSpends.put(customerId, customerSpends.get(customerId) - amount);
            salesAnalytics.add(new Cancel(i));
        }

        // Refund 10% of orders (partial refunds)
        for (int i = 2; i <= LARGE_DATASET_SIZE; i += 10) {
            String customerId = orderCustomers.get(i);
            double refundAmount = i * 5.0; // 50% refund
            customerSpends.put(customerId, customerSpends.get(customerId) - refundAmount);
            salesAnalytics.add(new Refund(i, refundAmount));
        }

        // Get expected top 10 customers
        List<String> expectedTopCustomers = customerSpends.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // When
        List<String> topCustomers = salesAnalytics.topNCustomersBySpend(10);

        // Then
        assertThat(topCustomers).containsExactlyElementsOf(expectedTopCustomers);
    }

    @Test
    @DisplayName("Should calculate average order value with large number of orders")
    void shouldCalculateAverageOrderValueWithLargeNumberOfOrders() {
        // Given
        Map<Channel, List<Double>> channelAmounts = new HashMap<>();
        for (Channel channel : Channel.values()) {
            channelAmounts.put(channel, new ArrayList<>());
        }

        // Add orders for each channel
        for (int i = 1; i <= LARGE_DATASET_SIZE; i++) {
            Channel channel = Channel.values()[i % Channel.values().length];
            double amount = i * 10.0;

            channelAmounts.get(channel).add(amount);

            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i),
                channel,
                "customer" + (i % 100),
                amount
            )));
        }

        // Calculate expected averages
        Map<Channel, Double> expectedAverages = new HashMap<>();
        for (Channel channel : Channel.values()) {
            List<Double> amounts = channelAmounts.get(channel);
            double sum = amounts.stream().mapToDouble(Double::doubleValue).sum();
            expectedAverages.put(channel, sum / amounts.size());
        }

        // When & Then
        for (Channel channel : Channel.values()) {
            OptionalDouble avgOrderValue = salesAnalytics.averageOrderValue(channel);
            assertThat(avgOrderValue).isPresent();
            assertThat(avgOrderValue.getAsDouble()).isEqualTo(expectedAverages.get(channel));
        }
    }

    @Test
    @DisplayName("Should calculate average order value with large number of orders and cancellations")
    void shouldCalculateAverageOrderValueWithLargeNumberOfOrdersAndCancellations() {
        // Given
        Map<Channel, List<Double>> channelAmounts = new HashMap<>();
        Map<Integer, Channel> orderChannels = new HashMap<>();

        for (Channel channel : Channel.values()) {
            channelAmounts.put(channel, new ArrayList<>());
        }

        // Add orders for each channel
        for (int i = 1; i <= LARGE_DATASET_SIZE; i++) {
            Channel channel = Channel.values()[i % Channel.values().length];
            orderChannels.put(i, channel);
            double amount = i * 10.0;

            channelAmounts.get(channel).add(amount);

            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i),
                channel,
                "customer" + (i % 100),
                amount
            )));
        }

        // Cancel 20% of orders
        for (int i = 1; i <= LARGE_DATASET_SIZE; i += 5) {
            Channel channel = orderChannels.get(i);
            channelAmounts.get(channel).remove(Double.valueOf(i * 10.0));
            salesAnalytics.add(new Cancel(i));
        }

        // Calculate expected averages
        Map<Channel, Double> expectedAverages = new HashMap<>();
        for (Channel channel : Channel.values()) {
            List<Double> amounts = channelAmounts.get(channel);
            if (amounts.isEmpty()) {
                expectedAverages.put(channel, 0.0); // No orders for this channel
            } else {
                double sum = amounts.stream().mapToDouble(Double::doubleValue).sum();
                expectedAverages.put(channel, sum / amounts.size());
            }
        }

        // When & Then
        for (Channel channel : Channel.values()) {
            OptionalDouble avgOrderValue = salesAnalytics.averageOrderValue(channel);
            if (channelAmounts.get(channel).isEmpty()) {
                assertThat(avgOrderValue).isEmpty();
            } else {
                assertThat(avgOrderValue).isPresent();
                assertThat(avgOrderValue.getAsDouble()).isEqualTo(expectedAverages.get(channel));
            }
        }
    }
}
