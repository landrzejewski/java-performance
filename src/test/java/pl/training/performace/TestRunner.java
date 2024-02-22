package pl.training.performace;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class TestRunner {

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(ReportsBenchmark.class.getSimpleName())
                .forks(1)
                .threads(1)
                .build();
        new Runner(options).run();
    }

}
