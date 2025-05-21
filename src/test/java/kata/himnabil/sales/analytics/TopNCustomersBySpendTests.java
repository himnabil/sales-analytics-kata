package kata.himnabil.sales.analytics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the topNCustomersBySpend method of the SalesAnalytics interface.
 */
@DisplayName("topNCustomersBySpend method tests")
class TopNCustomersBySpendTests extends BaseSalesAnalyticsTest {

    @Test
    @DisplayName("Should return empty list when no orders")
    void shouldReturnEmptyListWhenNoOrders() {
        assertThat(salesAnalytics.topNCustomersBySpend(5)).isEmpty();
    }

    @Test
    @DisplayName("Should return top customers by spend")
    void shouldReturnTopCustomersBySpend() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.WEB, "customer2", 300.0)));
        salesAnalytics.add(new NewOrder(new Order("3", Channel.MOBILE, "customer3", 200.0)));

        // When
        List<String> topCustomers = salesAnalytics.topNCustomersBySpend(2);

        // Then
        assertThat(topCustomers).containsExactly("customer2", "customer3");
    }

    @Test
    @DisplayName("Should account for refunds when calculating top customers")
    void shouldAccountForRefundsWhenCalculatingTopCustomers() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 300.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.WEB, "customer2", 200.0)));
        salesAnalytics.add(new NewOrder(new Order("3", Channel.MOBILE, "customer3", 100.0)));
        salesAnalytics.add(new Refund(1, 250.0));

        // When
        List<String> topCustomers = salesAnalytics.topNCustomersBySpend(3);

        // Then
        assertThat(topCustomers).containsExactly("customer2", "customer3", "customer1");
    }

    @Test
    @DisplayName("Should exclude cancelled orders when calculating top customers")
    void shouldExcludeCancelledOrdersWhenCalculatingTopCustomers() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 300.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.WEB, "customer2", 200.0)));
        salesAnalytics.add(new Cancel(1));

        // When
        List<String> topCustomers = salesAnalytics.topNCustomersBySpend(2);

        // Then
        assertThat(topCustomers).containsExactly("customer2");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 10})
    @DisplayName("Should limit results to N customers")
    void shouldLimitResultsToNCustomers(int n) {
        // Given
        for (int i = 1; i <= 10; i++) {
            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i), 
                Channel.WEB, 
                "customer" + i, 
                i * 100.0
            )));
        }

        // When
        List<String> topCustomers = salesAnalytics.topNCustomersBySpend(n);

        // Then
        assertThat(topCustomers).hasSize(Math.min(n, 10));

        // Verify the order is correct (highest spend first)
        for (int i = 0; i < topCustomers.size(); i++) {
            int expectedCustomerNumber = 10 - i;
            assertThat(topCustomers.get(i)).isEqualTo("customer" + expectedCustomerNumber);
        }
    }
}
