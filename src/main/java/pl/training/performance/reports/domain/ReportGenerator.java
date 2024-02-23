package pl.training.performance.reports.domain;

import pl.training.performance.reports.ports.DataProvider;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Predicate;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.*;
import static reactor.core.publisher.Flux.fromIterable;

public class ReportGenerator {

    private final DataProvider provider;
    public ReportGenerator(DataProvider provider) {
        this.provider = provider;
    }

    private Predicate<? super DataEntry> byYear(int year) {
        return dataEntry -> dataEntry.orderDate().getYear() == year;
    }

    private ProductStats toProductStats(Map.Entry<String, BigDecimal> entry) {
        return new ProductStats(entry.getKey(), entry.getValue());
    }

    public Flux<ProductStats> generateProductsRanging(int year) {
        return provider.findAll()
                .filter(byYear(year))
                .collect(groupingBy(DataEntry::itemType, mapping(DataEntry::totalProfit, reducing(ZERO, BigDecimal::add))))
                .flatMapMany(map -> fromIterable(map.entrySet()))
                .map(this::toProductStats)
                .sort();
    }

}
