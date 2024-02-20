package pl.training.performance;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class IO {

    private static final Path INPUT_PATH = Path.of("random2.data");
    private static final Path OUTPUT_PATH = Path.of("random-c2.data");
    private static final int BUFFER_SIZE = 512 * 1024;
    private static final ByteBuffer BUFFER = ByteBuffer.allocate(BUFFER_SIZE);

    @Setup(Level.Iteration)
    public void setup() throws IOException {
        Files.deleteIfExists(OUTPUT_PATH);
    }

    //@Benchmark
    public void copyWithStreams() throws IOException {
        try (InputStream inputStream = new FileInputStream(INPUT_PATH.toFile());
             OutputStream outputStream = new FileOutputStream(OUTPUT_PATH.toFile())) {
            int data;
            while ((data = inputStream.read()) != -1) {
                outputStream.write(data);
                outputStream.flush();
            }
        }
    }

    //@Benchmark
    public void copyWithStreamsNewApi() throws IOException {
        try (InputStream inputStream = Files.newInputStream(INPUT_PATH);
             OutputStream outputStream = Files.newOutputStream(OUTPUT_PATH)) {
            int data;
            while ((data = inputStream.read()) != -1) {
                outputStream.write(data);
                outputStream.flush();
            }
        }
    }

    // Buforowanie nie powinno być stosowane bezpośrednio na ByteArrayInput/OutpuStream - powoduje podwójne kopiowanie danych
    //@Benchmark
    public void copyWithBufferedStreams() throws IOException {
        try (InputStream inputStream = Files.newInputStream(INPUT_PATH);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
             OutputStream outputStream = Files.newOutputStream(OUTPUT_PATH);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER_SIZE)) {
            int data;
            while ((data = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(data);
            }
        }
    }

    //@Benchmark
    public void copyWithChannels() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(INPUT_PATH.toFile());
             FileChannel inputChannel = fileInputStream.getChannel();
             FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_PATH.toFile());
             FileChannel outputChannel = fileOutputStream.getChannel()) {
            // inputChannel.transferTo(0, inputChannel.size(), outputChannel);

            // outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

            while ((inputChannel.read(BUFFER)) != -1) {
                BUFFER.flip();
                outputChannel.write(BUFFER);
                BUFFER.clear();
            }
        }
    }

    @Benchmark
    public void copyWithMappedFiles() throws IOException {
        var buffer = new byte[BUFFER_SIZE];
        try (RandomAccessFile inputFile = new RandomAccessFile(INPUT_PATH.toFile(), "r");
             FileChannel inputChannel = inputFile.getChannel();
             RandomAccessFile outputFile = new RandomAccessFile(OUTPUT_PATH.toFile(), "rw");
             FileChannel outputChannel = outputFile.getChannel()) {
            MappedByteBuffer inputMappedFile = inputChannel.map(READ_ONLY, 0, inputChannel.size());
            MappedByteBuffer outputMappedFile = outputChannel.map(READ_WRITE, 0, inputChannel.size());
            while (inputMappedFile.hasRemaining()) {
                outputMappedFile.put(inputMappedFile.get(buffer));
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

/*
// File 20 MB
IO.copyWithStreams            ss       55,004           s/op
IO.copyWithStreamsNewApi      ss       57,838           s/op
IO.copyWithBufferedStreams    ss       0,275            s/op (default buffer)

// File 500 MB
IO.copyWithBufferedStreams    ss       10,873           s/op  (1 KB buffer)
IO.copyWithBufferedStreams    ss       7,174            s/op  (512 KB buffer)
IO.copyWithChannels           ss       0,847            s/op  (default buffer)
IO.copyWithChannels           ss       0,528            s/op  (512 KB buffer)
*/
