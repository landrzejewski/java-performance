package pl.training.performance.reports.adapters.provider.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.training.performance.reports.domain.DataEntry;
import pl.training.performance.reports.domain.PageSpec;
import pl.training.performance.reports.domain.ResultPage;
import pl.training.performance.reports.ports.DataProvider;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Transactional(propagation = MANDATORY)
@Repository
@RequiredArgsConstructor
public class SpringDataMongoAdapter implements DataProvider {

    private final SpringDataMongoProvider dataProvider;
    private final SpringDataMongoMappper dataMapper;
    private DataChangeDelegate dataChangeDelegate = () -> {};

    @Override
    public ResultPage<DataEntry> findAll(PageSpec pageSpec) {
        var pageRequest = PageRequest.of(pageSpec.pageNumber(), pageSpec.pageSize());
        var page = dataProvider.findAll(pageRequest);
        return dataMapper.toDomain(page);
    }

    @Override
    public void add(DataEntry dataEntry) {
       var entity = dataMapper.toDocument(dataEntry);
       dataProvider.save(entity);
       dataChangeDelegate.dataChanged();
    }

    @Override
    public void setDelegate(DataChangeDelegate dataChangeDelegate) {
        this.dataChangeDelegate = dataChangeDelegate;
    }

}
