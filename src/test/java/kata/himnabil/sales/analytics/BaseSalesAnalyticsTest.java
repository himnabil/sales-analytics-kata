package kata.himnabil.sales.analytics;

import org.junit.jupiter.api.BeforeEach;

/**
 * Base test class for SalesAnalytics tests.
 * Contains common setup and utility methods used by all test classes.
 */
public abstract class BaseSalesAnalyticsTest {
    
    protected SalesAnalytics salesAnalytics;
    
    @BeforeEach
    void setUp() {
        salesAnalytics = SalesAnalytics.create();
    }
}
