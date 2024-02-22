package pl.training.performance.reports;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.training.performance.reports.domain.ProductStats;
import pl.training.performance.reports.domain.ReportGenerator;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements ApplicationRunner {

    private final ReportGenerator reportGenerator;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var startTime = System.currentTimeMillis();
        var result = reportGenerator.generateProductsRanging(2012);
        System.out.println("Total time: " + (System.currentTimeMillis() - startTime));
        result.forEach(System.out::println);
    }

}
