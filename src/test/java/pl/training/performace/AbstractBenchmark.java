package pl.training.performace;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

abstract public class AbstractBenchmark {

    private final static Integer MEASUREMENT_ITERATIONS = 1;
    private final static Integer WARMUP_ITERATIONS = 3;

    @Test
    public void executeJmhRunner() throws RunnerException {
        var options = new OptionsBuilder()
            .include("\\." + this.getClass().getSimpleName() + "\\.")
            .forks(1)
            .threads(1)
            .build();
        new Runner(options).run();
    }

}
