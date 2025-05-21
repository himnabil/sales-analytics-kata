package kata.himnabil.sales.analytics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the add method of the SalesAnalytics interface.
 */
@DisplayName("add method tests")
class AddTests extends BaseSalesAnalyticsTest {

    @Test
    @DisplayName("Should add a new order event")
    void shouldAddNewOrderEvent() {
        // Given
        Order order = new Order("1", Channel.WEB, "customer1", 100.0);
        OrderEvent event = new NewOrder(order);

        // When
        salesAnalytics.add(event);

        // Then
        assertThat(salesAnalytics.totalNetRevenue()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Should add a cancel event")
    void shouldAddCancelEvent() {
        // Given
        Order order = new Order("1", Channel.WEB, "customer1", 100.0);
        salesAnalytics.add(new NewOrder(order));
        OrderEvent cancelEvent = new Cancel(1);

        // When
        salesAnalytics.add(cancelEvent);

        // Then
        assertThat(salesAnalytics.totalNetRevenue()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should add a refund event")
    void shouldAddRefundEvent() {
        // Given
        Order order = new Order("1", Channel.WEB, "customer1", 100.0);
        salesAnalytics.add(new NewOrder(order));
        OrderEvent refundEvent = new Refund(1, 50.0);

        // When
        salesAnalytics.add(refundEvent);

        // Then
        assertThat(salesAnalytics.totalNetRevenue()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("Should handle multiple events")
    void shouldHandleMultipleEvents() {
        // Given
        Order order1 = new Order("1", Channel.WEB, "customer1", 100.0);
        Order order2 = new Order("2", Channel.MOBILE, "customer2", 200.0);

        // When
        salesAnalytics.add(new NewOrder(order1));
        salesAnalytics.add(new NewOrder(order2));
        salesAnalytics.add(new Refund(1, 25.0));

        // Then
        assertThat(salesAnalytics.totalNetRevenue()).isEqualTo(275.0);
    }
}
