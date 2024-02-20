package pl.training.performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class Concatenation {

    private static ArrayList<String> values = new ArrayList<>(1_000);

    private String name = "Test";

    @Setup(Level.Iteration)
    public void setup() {
        for (int i = 0; i < 1_000; i++) {
            values.add(UUID.randomUUID().toString());
        }
    }

    @Benchmark
    public void basline(Blackhole blackhole) {
        String text = new StringBuilder(name).append("a").toString();
        blackhole.consume(text);
    }

    @Benchmark
    public void concatenationJava11(Blackhole blackhole) {
        String text = name + "a"; // new StringBuilder(name).append("a").toString();
        blackhole.consume(text);
    }

    @Benchmark
    public void concatenationJava8(Blackhole blackhole) {
        String text =  new StringBuilder().append(name).append("a").toString(); // różnica w zapisie StringBuilder + problem z konkatenacją double
        blackhole.consume(text);
    }

    @Benchmark
    public void concatenationInLoopJava11(Blackhole blackhole) {
        String text = "";
        for (int i = 0; i < values.size(); i++) {
            text = text + values.get(i);
        }
        blackhole.consume(text);
    }

    @Benchmark
    public void concatenationInLoopJava8(Blackhole blackhole) {
        String text = "";
        for (int i = 0; i < values.size(); i++) {
            text = new StringBuilder().append(text).append(values.get(i)).toString();
        }
        blackhole.consume(text);
    }

    @Benchmark
    public void concatenationCustom(Blackhole blackhole) {
        var text = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            text.append(values.get(i));
        }
        blackhole.consume(text.toString());
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(Concatenation.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(3)
                .threads(1)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}
