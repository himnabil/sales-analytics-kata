package kata.himnabil.sales.analytics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the netRevenueByChannel method of the SalesAnalytics interface.
 */
@DisplayName("netRevenueByChannel method tests")
class NetRevenueByChannelTests extends BaseSalesAnalyticsTest {

    @Test
    @DisplayName("Should return empty map when no orders")
    void shouldReturnEmptyMapWhenNoOrders() {
        assertThat(salesAnalytics.netRevenueByChannel()).isEmpty();
    }

    @Test
    @DisplayName("Should calculate revenue by channel")
    void shouldCalculateRevenueByChannel() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.WEB, "customer2", 200.0)));
        salesAnalytics.add(new NewOrder(new Order("3", Channel.MOBILE, "customer3", 300.0)));
        salesAnalytics.add(new NewOrder(new Order("4", Channel.STORE, "customer4", 400.0)));

        // When
        Map<Channel, Double> revenueByChannel = salesAnalytics.netRevenueByChannel();

        // Then
        assertThat(revenueByChannel)
            .containsEntry(Channel.WEB, 300.0)
            .containsEntry(Channel.MOBILE, 300.0)
            .containsEntry(Channel.STORE, 400.0);
    }

    @Test
    @DisplayName("Should subtract refunds from channel revenue")
    void shouldSubtractRefundsFromChannelRevenue() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.WEB, "customer2", 200.0)));
        salesAnalytics.add(new NewOrder(new Order("3", Channel.MOBILE, "customer3", 300.0)));
        salesAnalytics.add(new Refund(1, 50.0));

        // When
        Map<Channel, Double> revenueByChannel = salesAnalytics.netRevenueByChannel();

        // Then
        assertThat(revenueByChannel)
            .containsEntry(Channel.WEB, 250.0)
            .containsEntry(Channel.MOBILE, 300.0);
    }

    @Test
    @DisplayName("Should exclude cancelled orders from channel revenue")
    void shouldExcludeCancelledOrdersFromChannelRevenue() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.WEB, "customer2", 200.0)));
        salesAnalytics.add(new Cancel(1));

        // When
        Map<Channel, Double> revenueByChannel = salesAnalytics.netRevenueByChannel();

        // Then
        assertThat(revenueByChannel).containsEntry(Channel.WEB, 200.0);
    }

    @ParameterizedTest
    @EnumSource(Channel.class)
    @DisplayName("Should calculate revenue for each channel")
    void shouldCalculateRevenueForEachChannel(Channel channel) {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", channel, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", channel, "customer2", 200.0)));

        // When
        Map<Channel, Double> revenueByChannel = salesAnalytics.netRevenueByChannel();

        // Then
        assertThat(revenueByChannel).containsEntry(channel, 300.0);
    }
}
