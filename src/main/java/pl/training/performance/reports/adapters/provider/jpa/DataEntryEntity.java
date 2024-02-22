package pl.training.performance.reports.adapters.provider.jpa;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.training.performance.reports.domain.OrderPriority;

import java.math.BigDecimal;
import java.time.LocalDate;

import static pl.training.performance.reports.adapters.provider.jpa.DataEntryEntity.FIND_BY_ITEM_TYPE;

@NamedQuery(name = FIND_BY_ITEM_TYPE, query = "select de from DataEntry de where itemType = :itemType")
@Table(name = "entries") //, indexes = @Index(name = "DataEntry_itemType", columnList = "itemType"))
@Entity(name = "DataEntry")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class DataEntryEntity {

    public static final String FIND_BY_ITEM_TYPE = "DataEntry.FIND_BY_ITEM_TYPE";

    @Id
    private long orderId;
    @Column(length = 40)
    private String region;
    @Column(length = 40)
    private String country;
    @Column(length = 40)
    private String itemType;
    private boolean isOnlineSaleChannel;
    @Enumerated(EnumType.ORDINAL)
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
