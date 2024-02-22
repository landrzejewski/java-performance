package pl.training.performance.reports.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.*;

public class ProcesProductsTask extends RecursiveTask<Map<String, BigDecimal>> {

    private final ResultPage<DataEntry> entries;
    private final int year;

    public ProcesProductsTask(ResultPage<DataEntry> entries, int year) {
        this.entries = entries;
        this.year = year;
    }

    @Override
    protected Map<String, BigDecimal> compute() {
        return entries.getRows()
                .stream()
                .filter(dataEntry -> dataEntry.orderDate().getYear() == year)
                .collect(groupingBy(DataEntry::itemType, mapping(DataEntry::totalProfit, reducing(ZERO, BigDecimal::add))));
    }

}
