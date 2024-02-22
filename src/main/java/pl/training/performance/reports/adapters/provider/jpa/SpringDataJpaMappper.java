package pl.training.performance.reports.adapters.provider.jpa;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import pl.training.performance.reports.domain.DataEntry;
import pl.training.performance.reports.domain.ResultPage;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpringDataJpaMappper {

    DataEntry toDomain(DataEntryEntity dataEntryEntity);

    @IterableMapping(elementTargetType = DataEntry.class)
    List<DataEntry> toDomain(List<DataEntryEntity> dataEntryEntities);

    @Mapping(source = "content", target = "rows")
    ResultPage<DataEntry> toDomain(Page<DataEntryEntity> dataEntryEntities);

    DataEntryEntity toEntity(DataEntry dataEntry);

}
