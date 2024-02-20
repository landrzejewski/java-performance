package pl.training.performance.jmh;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Benchmarks {

    // -Djmh.blackhole.autoDetect=false - disables the need to automatically apply blackhole wrappers
    // -rff results.csv - save results to file
    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                //.include("testMethod")
                .include(JMH_07_BranchPrediction.class.getSimpleName())
                .warmupIterations(1)
                .measurementIterations(2)
                .threads(1)
                .forks(1)
                .build();
        var result = new Runner(options).run();
       /* result.stream()
                .findFirst()
                .get()
                .getBenchmarkResults();*/
    }

}
