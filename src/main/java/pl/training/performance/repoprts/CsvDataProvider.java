package pl.training.performance.repoprts;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static pl.training.performance.repoprts.OrderPriority.*;

public class CsvDataProvider implements DataProvider {

    private static final int START_POSITION = 0;
    private static final byte EMPTY_VALUE = 0x0;
    private static final int RECORD_SIZE = 200;

    private static final String FIELD_SEPARATOR = ",";
    private static final String ONLINE_MARKER = "Online";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    private final RandomAccessFile randomAccessFile;

    public CsvDataProvider(Path filePath) {
        try {
            this.randomAccessFile = new RandomAccessFile(filePath.toFile(), "rw");
        } catch (FileNotFoundException e) {
            throw new DataLoadingFailedException();
        }
        /*try (var lines = Files.lines(filePath).skip(1)) {
            this.randomAccessFile = new RandomAccessFile(new File("sales.data"), "rw");
            lines.forEach(withCounter(this::write));
        } catch (IOException e) {
            throw new DataLoadingFailedException();
        }*/
    }

    @SneakyThrows
    private void write(long lineNo, String line) {
        var buffer = ByteBuffer.allocate(RECORD_SIZE);
        buffer.put(toField(line.trim().getBytes(), RECORD_SIZE));
        var bytes = buffer.array();
        randomAccessFile.seek(lineNo * RECORD_SIZE);
        randomAccessFile.write(bytes);
    }

    public static <T> Consumer<T> withCounter(BiConsumer<Long, T> consumer) {
        var counter = new AtomicLong(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }

    private byte[] toField(byte[] data, int size) {
        var bytes = new byte[size];
        System.arraycopy(data, START_POSITION, bytes, START_POSITION, data.length);
        Arrays.fill(bytes, data.length, size, EMPTY_VALUE);
        return bytes;
    }

    private DataEntry toDataEntry(String row) {
        var fields = row.split(FIELD_SEPARATOR);
        var region = fields[0];
        var country = fields[1];
        var itemType = fields[2];
        var isOnlineSaleChannel = fields[3].equals(ONLINE_MARKER);
        var orderPriority = switch (fields[4]) {
            case "C" -> CRITICAL;
            case "H" -> HIGH;
            case "M" -> MEDIUM;
            default -> LOW;
        };
        var orderDate = LocalDate.parse(fields[5], DATE_TIME_FORMATTER);
        var orderId = Long.parseLong(fields[6]);
        var shipDate = LocalDate.parse(fields[7], DATE_TIME_FORMATTER);
        var unitsSold = Long.parseLong(fields[8]);
        var unitPrice = new BigDecimal(fields[9]);
        var unitCost = new BigDecimal(fields[10]);
        var totalRevenue = new BigDecimal(fields[11]);
        var totalCost = new BigDecimal(fields[12]);
        var totalProfit = new BigDecimal(fields[13]);
        return new DataEntry(region, country, itemType, isOnlineSaleChannel, orderPriority, orderDate, orderId, shipDate,
                unitsSold, unitPrice, unitCost, totalRevenue, totalCost, totalProfit);
    }

    @SneakyThrows
    @Override
    public ResultPage<DataEntry> findAll(PageSpec pageSpec) {
        var rows = new ArrayList<DataEntry>(pageSpec.pageSize());
        var offset = (long) pageSpec.getOffset() * RECORD_SIZE;
        //var bytes = new byte[RECORD_SIZE * pageSpec.pageSize()];
        var bytes = new byte[RECORD_SIZE];
        for (int recordNo = 0; recordNo < pageSpec.pageSize(); recordNo++) {
            randomAccessFile.seek(offset);
            randomAccessFile.read(bytes);
            var line = new String(bytes).trim();
            if (line.isEmpty()) {
                break;
            }
            var entry = toDataEntry(line);
            rows.add(entry);
            offset += RECORD_SIZE;
        }
       /* for (int recordNo = 0; recordNo < pageSpec.pageSize(); recordNo++) {
            var recordOffset = RECORD_SIZE * recordNo;
            var lineBytes = Arrays.copyOfRange(bytes, recordOffset, recordOffset + RECORD_SIZE);
            var line = new String(lineBytes).trim();
            if (line.isEmpty()) {
                break;
            }
            var entry = toDataEntry(line);
            rows.add(entry);
        }*/
        var totalPages = (int) Math.ceil((double) randomAccessFile.length() / pageSpec.pageSize());
        return new ResultPage<>(rows, totalPages);
    }

}
