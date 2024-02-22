package pl.training.performance.reports.domain;

import java.math.BigDecimal;

public record ProductStats(String itemType, BigDecimal totalProfit) implements Comparable<ProductStats> {

    @Override
    public int compareTo(ProductStats productStats) {
        return totalProfit.compareTo(productStats.totalProfit);
    }

}
