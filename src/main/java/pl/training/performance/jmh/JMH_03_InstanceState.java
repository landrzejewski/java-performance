package pl.training.performance.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class JMH_03_InstanceState {

    volatile int value;

    @Setup(Level.Iteration)
    public void setup(Blackhole blackhole, BenchmarkParams benchmarkParams) {
        // blackhole.consumeCPU(10);
        // benchmarkParams.getThreads();
        value = 1;
    }

    @TearDown(Level.Trial)
    public void clean() {
        value = 1;
    }

    @Benchmark
    public void testWithState() {
        value++;
        System.out.println(value);
    }

}
