package pl.training.performance.reports;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DataEntry(
        String region,
        String country,
        String itemType,
        boolean isOnlineSaleChannel,
        OrderPriority orderPriority,
        LocalDate orderDate,
        long orderId,
        LocalDate shipDate,
        long unitsSold,
        BigDecimal unitPrice,
        BigDecimal unitCost,
        BigDecimal totalRevenue,
        BigDecimal totalCost,
        BigDecimal totalProfit
) {
}
