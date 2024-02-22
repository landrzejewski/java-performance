package pl.training.performace;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.training.performance.reports.Application;
import pl.training.performance.reports.domain.ProductStats;
import pl.training.performance.reports.domain.ReportGenerator;

import java.util.List;

@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class ReportsBenchmark {

    private ConfigurableApplicationContext context;
    private ReportGenerator reportGenerator;

    @Setup
    public void setup() {
        context = SpringApplication.run(Application.class);
        reportGenerator = context.getBean(ReportGenerator.class);
    }

    @TearDown
    public void teardown() {
        context.close();
    }

    @Benchmark
    public List<ProductStats> testReport() {
        return reportGenerator.generateProductsRanging(2012);
    }

}
