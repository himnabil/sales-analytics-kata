package kata.himnabil.sales.analytics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Main test class for SalesAnalytics.
 * <p>
 * This class contains basic tests to verify the SalesAnalytics implementation.
 * More detailed tests are in separate test classes:
 * - {@link AddTests} - Tests for the add method
 * - {@link TotalNetRevenueTests} - Tests for the totalNetRevenue method
 * - {@link NetRevenueByChannelTests} - Tests for the netRevenueByChannel method
 * - {@link TopNCustomersBySpendTests} - Tests for the topNCustomersBySpend method
 * - {@link AverageOrderValueTests} - Tests for the averageOrderValue method
 * - {@link HeavyDataTests} - Tests with large datasets
 */
@DisplayName("SalesAnalytics tests")
class SalesAnalyticsTest extends BaseSalesAnalyticsTest {

    @Test
    @DisplayName("Should verify basic functionality")
    void shouldVerifyBasicFunctionality() {
        // Given
        Order order = new Order("1", Channel.WEB, "customer1", 100.0);
        
        // When
        salesAnalytics.add(new NewOrder(order));
        
        // Then
        assertThat(salesAnalytics.totalNetRevenue()).isEqualTo(100.0);
        assertThat(salesAnalytics.netRevenueByChannel()).containsEntry(Channel.WEB, 100.0);
        assertThat(salesAnalytics.topNCustomersBySpend(1)).containsExactly("customer1");
        assertThat(salesAnalytics.averageOrderValue(Channel.WEB).getAsDouble()).isEqualTo(100.0);
    }
}
