package pl.training.performance.reports;

import pl.training.performance.reports.provider.RandomAccessDataProvider;

import java.nio.file.Path;

public class Application {

    public static void main(String[] args) {
        var filePath = Path.of("sales.data");
        var dataProvider = new RandomAccessDataProvider(filePath);
        var reportGenerator = new ReportGenerator(dataProvider);
        reportGenerator.generateProductsRanging(2012)
                .forEach(System.out::println);
    }

}
