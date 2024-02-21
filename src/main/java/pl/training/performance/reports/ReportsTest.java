package pl.training.performance.reports;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import pl.training.performance.reports.provider.EagerCsvDataProvider;
import pl.training.performance.reports.provider.RandomAccessDataProvider;
import pl.training.performance.reports.provider.SynchronizedDataProvider;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static java.math.BigDecimal.TEN;
import static pl.training.performance.reports.OrderPriority.MEDIUM;

@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class ReportsTest {

    private static final Path FILE_PATH = Path.of("sales.data");

    private SynchronizedDataProvider dataProvider;
    private ReportGenerator reportGenerator;

    @Setup(Level.Iteration)
    public void setup() {
        var randomAccessDataProvider = new RandomAccessDataProvider(FILE_PATH);
        //var eagerDataProvider = new EagerCsvDataProvider(Path.of("5m Sales Records.csv"));
        dataProvider = new SynchronizedDataProvider(randomAccessDataProvider);
        reportGenerator = new ReportGenerator(dataProvider);
    }

    @TearDown(Level.Iteration)
    public void teardown() {
        dataProvider.close();
    }

    // @Benchmark
    public ResultPage<DataEntry> dataLoad() {
        return dataProvider.findAll(new PageSpec(0, 500_000));
    }

    @Benchmark
    public List<ProductStats> dataLoadAndReporting() {
        return new CacheableReportGenerator(reportGenerator)
                .generateProductsRanging(2012);
    }

    // @Benchmark
    public void addRecord() {
        var entry = new DataEntry("X", "X", "X", true, MEDIUM, LocalDate.now(), 999,
                LocalDate.now(), 2, TEN, TEN, TEN, TEN, TEN);
        var cacheableGenerator = new CacheableReportGenerator(reportGenerator);
        dataProvider.add(entry);
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
ReportsTest.dataLoadAndReporting    ss       8,234            s/op  RandomAccess (full block)
ReportsTest.dataLoadAndReporting    ss       8,827            s/op  SynchronizedDataProvider + LinkedHashMapCache
 */
