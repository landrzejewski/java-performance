package pl.training.performance.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_06_Parameters {

    @Param({"1.9", "2.0", "2.0"})
    private double x ;

    private double compute(double d) {
        for (int c = 0; c < 10; c++) {
            d = d * d / Math.PI;
        }
        return d;
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE) // compiler config options
    // -p x=1.9,2.0,2.0
    @Benchmark
    public double testCompute() {
        return compute(x);
    }

}

