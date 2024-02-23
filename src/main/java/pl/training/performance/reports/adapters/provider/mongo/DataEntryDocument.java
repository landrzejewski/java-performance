package pl.training.performance.reports.adapters.provider.mongo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.training.performance.reports.domain.OrderPriority;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Document
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class DataEntryDocument {

    @Id
    private String id;
    private long orderId;
    private String region;
    private String country;
    private String itemType;
    private boolean isOnlineSaleChannel;
    private OrderPriority orderPriority;
    private LocalDate orderDate;
    private LocalDate shipDate;
    long unitsSold;
    private BigDecimal unitPrice;
    private BigDecimal unitCost;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;

}
