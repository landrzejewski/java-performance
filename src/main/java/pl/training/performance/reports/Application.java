package pl.training.performance.reports;

import pl.training.performance.reports.provider.RandomAccessDataProvider;
import pl.training.performance.reports.provider.SynchronizedDataProvider;

import java.nio.file.Path;

public class Application {

    public static void main(String[] args) {
        var filePath = Path.of("sales.data");
        try (var dataProvider = new RandomAccessDataProvider(filePath)) {
            var synchronizedDataProvider = new SynchronizedDataProvider(dataProvider);
            var reportGenerator = new ReportGenerator(synchronizedDataProvider);
            var cacheableReportGenerator = new CacheableReportGenerator(reportGenerator);
            cacheableReportGenerator.generateProductsRanging(2012)
                    .forEach(System.out::println);
        }
    }

}
