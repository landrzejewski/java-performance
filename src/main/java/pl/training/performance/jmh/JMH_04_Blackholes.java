package pl.training.performance.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_04_Blackholes {

    private double x = Math.PI;

    private double compute(double d) {
        for (int c = 0; c < 10; c++) {
            d = d * d / Math.PI;
        }
        return d;
    }

    @Benchmark
    public void baseline() {
        // do nothing, this is a baseline
    }

    @Benchmark
    public void testWithDeadCode() {
        compute(x);
    }

    @Benchmark
    public double testWithReturnValue() {
        return compute(x); // return prevents dead code elimination
    }

    @Benchmark
    public void testWithBlackhole(Blackhole blackhole) {
        blackhole.consume(compute(x)); // return prevents dead code elimination
    }

}
