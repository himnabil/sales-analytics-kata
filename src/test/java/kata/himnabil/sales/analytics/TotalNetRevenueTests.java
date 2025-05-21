package kata.himnabil.sales.analytics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the totalNetRevenue method of the SalesAnalytics interface.
 */
@DisplayName("totalNetRevenue method tests")
class TotalNetRevenueTests extends BaseSalesAnalyticsTest {

    @Test
    @DisplayName("Should return zero when no orders")
    void shouldReturnZeroWhenNoOrders() {
        assertThat(salesAnalytics.totalNetRevenue()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should calculate total revenue from multiple orders")
    void shouldCalculateTotalRevenueFromMultipleOrders() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.MOBILE, "customer2", 200.0)));
        salesAnalytics.add(new NewOrder(new Order("3", Channel.STORE, "customer3", 300.0)));

        // When
        double totalRevenue = salesAnalytics.totalNetRevenue();

        // Then
        assertThat(totalRevenue).isEqualTo(600.0);
    }

    @Test
    @DisplayName("Should subtract refunds from total revenue")
    void shouldSubtractRefundsFromTotalRevenue() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.MOBILE, "customer2", 200.0)));
        salesAnalytics.add(new Refund(1, 50.0));

        // When
        double totalRevenue = salesAnalytics.totalNetRevenue();

        // Then
        assertThat(totalRevenue).isEqualTo(250.0);
    }

    @Test
    @DisplayName("Should exclude cancelled orders from total revenue")
    void shouldExcludeCancelledOrdersFromTotalRevenue() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.MOBILE, "customer2", 200.0)));
        salesAnalytics.add(new Cancel(1));

        // When
        double totalRevenue = salesAnalytics.totalNetRevenue();

        // Then
        assertThat(totalRevenue).isEqualTo(200.0);
    }

    @ParameterizedTest
    @CsvSource({
        "100.0, 200.0, 300.0, 50.0, 25.0, 525.0",
        "200.0, 300.0, 400.0, 100.0, 50.0, 750.0",
        "0.0, 0.0, 0.0, 0.0, 0.0, 0.0"
    })
    @DisplayName("Should calculate correct net revenue for various scenarios")
    void shouldCalculateCorrectNetRevenueForVariousScenarios(
            double order1Amount, double order2Amount, double order3Amount,
            double refund1Amount, double refund2Amount, double expectedNetRevenue) {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", order1Amount)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.MOBILE, "customer2", order2Amount)));
        salesAnalytics.add(new NewOrder(new Order("3", Channel.STORE, "customer3", order3Amount)));
        salesAnalytics.add(new Refund(1, refund1Amount));
        salesAnalytics.add(new Refund(2, refund2Amount));

        // When
        double totalRevenue = salesAnalytics.totalNetRevenue();

        // Then
        assertThat(totalRevenue).isEqualTo(expectedNetRevenue);
    }
}
