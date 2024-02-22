package pl.training.performance.reports.adapters.provider.mongo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import pl.training.performance.reports.adapters.provider.jpa.DataEntryEntity;
import pl.training.performance.reports.domain.DataEntry;
import pl.training.performance.reports.domain.ResultPage;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpringDataMongoMappper {

    DataEntryEntity toDomain(DataEntryDocument dataEntryDocument);

    @IterableMapping(elementTargetType = DataEntry.class)
    List<DataEntry> toDomain(List<DataEntryDocument> dataEntryDocuments);

    @Mapping(source = "content", target = "rows")
    ResultPage<DataEntry> toDomain(Page<DataEntryDocument> dataEntryDocuments);

    DataEntryDocument toDocument(DataEntry dataEntry);

}
