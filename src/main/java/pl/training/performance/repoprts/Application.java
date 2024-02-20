package pl.training.performance.repoprts;

import java.nio.file.Path;

public class Application {

    public static void main(String[] args) {
        var filePath = Path.of("5m Sales Records.csv");
        var dataProvider = new CsvDataProvider(filePath);
        var reportGenerator = new ReportGenerator(dataProvider);
        reportGenerator.generateProductsRanging(2012)
                .forEach(System.out::println);
    }

}
