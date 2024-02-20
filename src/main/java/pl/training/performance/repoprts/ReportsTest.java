package pl.training.performance.repoprts;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.file.Path;

@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class ReportsTest {

    @Benchmark
    public ResultPage<DataEntry> dataLoad() {
        var dataProvider = new CsvDataProvider( Path.of("5m Sales Records.csv"));
        return dataProvider.findAll(new PageSpec(0, 1_000));
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
