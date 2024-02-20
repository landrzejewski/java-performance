package pl.training.performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SampleTime)
@State(Scope.Benchmark)
public class IO {

    private static final Path INPUT_PATH = Path.of("random.data");
    private static final Path OUTPUTH_PATH = Path.of("random-copy.data");

    @Setup(Level.Iteration)
    public void setup() throws IOException {
        Files.deleteIfExists(OUTPUTH_PATH);
    }

    @Benchmark
    public void copyWithStreams() throws IOException {
        try (InputStream inputStream = new FileInputStream(INPUT_PATH.toFile());
             OutputStream outputStream = new FileOutputStream(OUTPUTH_PATH.toFile())) {
            int data;
            while ((data = inputStream.read()) != -1) {
                outputStream.write(data);
                outputStream.flush();
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(IO.class.getSimpleName())
                .threads(1)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}
