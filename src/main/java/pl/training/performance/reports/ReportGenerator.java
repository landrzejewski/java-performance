package pl.training.performance.reports;

import pl.training.performance.reports.provider.DataProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import static java.math.BigDecimal.ZERO;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static java.util.stream.Collectors.*;

public class ReportGenerator {

    private static final int PAGE_SIZE = 500_000;

    private final DataProvider provider;
    private final ForkJoinPool processingPool = commonPool();

    public ReportGenerator(DataProvider provider) {
        this.provider = provider;
    }

    public List<ProductStats> generateProductsRanging(int year) {
        var resultPage = provider.findAll(new PageSpec(0, PAGE_SIZE));
        var totalPages = resultPage.getTotalPages();

        var tasks = new ArrayList<ProcesProductsTask>();

        var firstTask = new ProcesProductsTask(resultPage, year);
        processingPool.execute(firstTask);

        //var partialResult = process(resultPage, year);
        var partialResult = new HashMap<String, BigDecimal>();

        for (int pageNumber = 1; pageNumber < totalPages; pageNumber++) {
            var page = provider.findAll(new PageSpec(pageNumber, PAGE_SIZE));
            //process(page, year).forEach((key, value) -> partialResult.merge(key, value, BigDecimal::add));
            var task = new ProcesProductsTask(page, year);
            tasks.add(task);
            processingPool.execute(task);
        }

        tasks.stream()
                .map(ForkJoinTask::join)
                .forEach(results -> results.forEach((key, value) -> partialResult.merge(key, value, BigDecimal::add)));

        return partialResult
                .entrySet()
                .stream()
                .map(entry -> new ProductStats(entry.getKey(), entry.getValue()))
                .sorted()
                .toList();
    }

    private Map<String, BigDecimal> process(ResultPage<DataEntry> entries, int year) {
        return entries.getRows()
                .stream()
                .filter(dataEntry -> dataEntry.orderDate().getYear() == year)
                .collect(groupingBy(DataEntry::itemType, mapping(DataEntry::totalProfit, reducing(ZERO, BigDecimal::add))));
    }

    public DataProvider getProvider() {
        return provider;
    }

}
