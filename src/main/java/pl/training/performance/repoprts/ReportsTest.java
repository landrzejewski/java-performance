package pl.training.performance.repoprts;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.file.Path;
import java.util.List;

@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class ReportsTest {

    private static final Path FILE_PATH = Path.of("5m Sales Records.csv");

    // cache

    @Benchmark
    public ResultPage<DataEntry> dataLoad() {
        var dataProvider = new CsvDataProvider(FILE_PATH);
        return dataProvider.findAll(new PageSpec(0, 500_000));
    }

    //@Benchmark
    public List<ProductStats> dataLoadAndReporting() {
        var dataProvider = new CsvDataProvider(FILE_PATH);
        var reportGenerator = new ReportGenerator(dataProvider);
        return reportGenerator.generateProductsRanging(2012);
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(ReportsTest.class.getSimpleName())
                .threads(1)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}

/*

ReportsTest.dataLoad                ss       13,340           s/op
ReportsTest.dataLoad                ss       1,498            s/op


ReportsTest.dataLoadAndReporting    ss       16,408           s/op  (pageSize 50 -> 1_000_000)
 */
