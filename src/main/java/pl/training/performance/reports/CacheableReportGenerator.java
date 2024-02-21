package pl.training.performance.reports;

import pl.training.performance.reports.provider.DataProvider;

import java.util.List;

public class CacheableReportGenerator {

    public static final int DEFAULT_CACHE_SIZE = 10;

    private final ReportGenerator reportGenerator;
    private final LinkedHashMapCache<Integer, List<ProductStats>> productStatsCache;

    public CacheableReportGenerator(ReportGenerator reportGenerator, int cacheSize) {
        this.reportGenerator = reportGenerator;
        this.productStatsCache = new LinkedHashMapCache<>(cacheSize);
        reportGenerator.getProvider().setDelegate(productStatsCache::clear);
    }

    public CacheableReportGenerator(ReportGenerator reportGenerator) {
        this(reportGenerator, DEFAULT_CACHE_SIZE);
    }

    public List<ProductStats> generateProductsRanging(int year) {
        return productStatsCache.get(year)
                .orElseGet(() -> supplyProductStats(year));
    }

    private List<ProductStats> supplyProductStats(int year) {
        var productStats = reportGenerator.generateProductsRanging(year);
        productStatsCache.put(year, productStats);
        return productStats;
    }

}
