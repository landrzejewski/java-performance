package pl.training.performance.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_05_ConstantFoldAndLooping {

    private double x = Math.PI;

    private final double wrongX = Math.PI;

    private double compute(double d) {
        for (int c = 0; c < 10; c++) {
            d = d * d / Math.PI;
        }
        return d;
    }

    @Benchmark
    public double baseline() {
        return Math.PI;
    }

    @Benchmark
    public double resultCaching() {
        return compute(Math.PI); // This is wrong: the source is predictable, and computation is foldable
    }

    @Benchmark
    public double resultCaching2() {
        return compute(wrongX); // This is wrong: the source is predictable, and computation is foldable
    }

    @Benchmark
    public double noCaching() {
        return compute(x); // This is correct: the source is not predictable
    }


    int a = 1;
    int b = 2;

    /*
     * The following tests emulate the naive looping.
     * This is the Caliper-style benchmark.
     */
    private int reps(int reps) {
        int s = 0;
        for (int i = 0; i < reps; i++) {
            s += (a + b);
        }
        return s;
    }


    @Benchmark
    public int loopingWithJmh() {
        return a + b; // Ok
    }

    @Benchmark
    @OperationsPerInvocation(1)
    public int loopingWithAdditionalLoop_1() {
        return reps(1); // Not ok
    }

    @Benchmark
    @OperationsPerInvocation(100)
    public int loopingWithAdditionalLoop_100() {
        return reps(100); // Not ok
    }

    @Benchmark
    @OperationsPerInvocation(1_000)
    public int loopingWithAdditionalLoop_1000() {
        return reps(1_000); // Not ok
    }
}

