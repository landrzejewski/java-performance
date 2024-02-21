package pl.training.performance.reports.provider;

import pl.training.performance.reports.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static pl.training.performance.reports.OrderPriority.*;

public class CsvDataProvider implements DataProvider {

    private static final String FIELD_SEPARATOR = ",";
    private static final String ONLINE_MARKER = "Online";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    private final Path filePath;
    private final double totalEntries;

    public CsvDataProvider(Path filePath) {
        this.filePath = filePath;
        try (var lines = Files.lines(filePath)) {
            totalEntries = lines.skip(1).count();
        } catch (IOException ioException) {
            throw new DataLoadingFailedException();
        }
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

    @Override
    public ResultPage<DataEntry> findAll(PageSpec pageSpec) {
        try {
            Files.lines(filePath).skip(pageSpec.getOffset())
                    .forEach(string -> System.out.println(string.getBytes(StandardCharsets.UTF_8).length));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var lines = Files.lines(filePath)) {
            var rows = lines.skip(1)
                    .skip(pageSpec.getOffset())
                    .limit(pageSpec.pageSize())
                    .map(this::toDataEntry)
                    .toList();
            var totalPages = (int) Math.ceil(totalEntries / pageSpec.pageSize());
            return new ResultPage<>(rows, totalPages);
        } catch (IOException ioException) {
            throw new DataLoadingFailedException();
        }
    }

}