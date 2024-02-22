package pl.training.performace;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.test.context.SpringBootTest;
import pl.training.performance.reports.Application;

@SpringBootTest(classes = Application.class)
@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class ReportsBenchmark extends AbstractBenchmark {

    @Setup(Level.Trial)
    public void setupBenchmark() {
    }

    @Benchmark
    public void someBenchmarkMethod() {
    }

}
