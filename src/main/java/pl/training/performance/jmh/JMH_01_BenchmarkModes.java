package pl.training.performance.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

// @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
public class JMH_01_BenchmarkModes {

    // Mode.Throughput measures the raw throughput by continuously calling the benchmark method in a time-bound iteration
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(200);
    }

    // Mode.AverageTime measures the average method execution time
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testAvgTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(200);
    }

    //  Mode.SampleTime samples the execution time. With this mode, we are still running the method in a time-bound iteration, but instead of
    //  measuring the total time, we measure the time spent in *some* of the benchmark method calls
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSampleTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(200);
    }

    // Mode.SingleShotTime measures the single method invocation time
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testSingleShotTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(200);
    }

    /*
    @Benchmark
    // @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testAll() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(200);
    }
    */

}
