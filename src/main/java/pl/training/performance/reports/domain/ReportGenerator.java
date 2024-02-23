package pl.training.performance.reports.domain;

import pl.training.performance.reports.ports.DataProvider;
import reactor.core.publisher.Flux;

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

    private final DataProvider provider;
    public ReportGenerator(DataProvider provider) {
        this.provider = provider;
    }

    public Flux<ProductStats> generateProductsRanging(int year) {
        return provider.findAll()
                .


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

}
