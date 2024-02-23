package pl.training.performance.reports;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.training.performance.reports.domain.ReportGenerator;

import java.text.DecimalFormat;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements ApplicationRunner {

    private final ReportGenerator reportGenerator;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        var startTime = System.currentTimeMillis();
        reportGenerator.generateProductsRanging(2012)
                .subscribe(
                        System.out::println,
                        System.out::println,
                        () -> {
                            var formatter = new DecimalFormat("###,###,###");
                            var time = System.currentTimeMillis() - startTime;
                            System.out.println("Total time: %sms".formatted(formatter.format(time)));
                        });
    }

}
