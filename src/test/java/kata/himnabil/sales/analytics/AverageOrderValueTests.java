package kata.himnabil.sales.analytics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the averageOrderValue method of the SalesAnalytics interface.
 */
@DisplayName("averageOrderValue method tests")
class AverageOrderValueTests extends BaseSalesAnalyticsTest {

    @Test
    @DisplayName("Should return empty when no orders for channel")
    void shouldReturnEmptyWhenNoOrdersForChannel() {
        assertThat(salesAnalytics.averageOrderValue(Channel.WEB)).isEmpty();
    }

    @Test
    @DisplayName("Should calculate average order value for channel")
    void shouldCalculateAverageOrderValueForChannel() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.WEB, "customer2", 200.0)));
        salesAnalytics.add(new NewOrder(new Order("3", Channel.MOBILE, "customer3", 300.0)));

        // When
        OptionalDouble avgOrderValue = salesAnalytics.averageOrderValue(Channel.WEB);

        // Then
        assertThat(avgOrderValue).isPresent();
        assertThat(avgOrderValue.getAsDouble()).isEqualTo(150.0);
    }

    @Test
    @DisplayName("Should exclude cancelled orders when calculating average")
    void shouldExcludeCancelledOrdersWhenCalculatingAverage() {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", Channel.WEB, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", Channel.WEB, "customer2", 300.0)));
        salesAnalytics.add(new Cancel(1));

        // When
        OptionalDouble avgOrderValue = salesAnalytics.averageOrderValue(Channel.WEB);

        // Then
        assertThat(avgOrderValue).isPresent();
        assertThat(avgOrderValue.getAsDouble()).isEqualTo(300.0);
    }

    @ParameterizedTest
    @EnumSource(Channel.class)
    @DisplayName("Should calculate average for each channel")
    void shouldCalculateAverageForEachChannel(Channel channel) {
        // Given
        salesAnalytics.add(new NewOrder(new Order("1", channel, "customer1", 100.0)));
        salesAnalytics.add(new NewOrder(new Order("2", channel, "customer2", 300.0)));

        // When
        OptionalDouble avgOrderValue = salesAnalytics.averageOrderValue(channel);

        // Then
        assertThat(avgOrderValue).isPresent();
        assertThat(avgOrderValue.getAsDouble()).isEqualTo(200.0);
    }

    @ParameterizedTest
    @MethodSource("averageOrderValueTestCases")
    @DisplayName("Should calculate correct average for various scenarios")
    void shouldCalculateCorrectAverageForVariousScenarios(
            List<Double> orderAmounts, Channel channel, double expectedAverage) {
        // Given
        for (int i = 0; i < orderAmounts.size(); i++) {
            salesAnalytics.add(new NewOrder(new Order(
                String.valueOf(i + 1), 
                channel, 
                "customer" + (i + 1), 
                orderAmounts.get(i)
            )));
        }

        // When
        OptionalDouble avgOrderValue = salesAnalytics.averageOrderValue(channel);

        // Then
        assertThat(avgOrderValue).isPresent();
        assertThat(avgOrderValue.getAsDouble()).isEqualTo(expectedAverage);
    }

    static Stream<Arguments> averageOrderValueTestCases() {
        return Stream.of(
            Arguments.of(List.of(100.0, 200.0, 300.0), Channel.WEB, 200.0),
            Arguments.of(List.of(50.0, 150.0), Channel.MOBILE, 100.0),
            Arguments.of(List.of(1000.0), Channel.STORE, 1000.0)
        );
    }
}
