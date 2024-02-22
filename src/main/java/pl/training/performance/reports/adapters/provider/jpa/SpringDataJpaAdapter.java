package pl.training.performance.reports.adapters.provider.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.training.performance.reports.domain.DataEntry;
import pl.training.performance.reports.domain.PageSpec;
import pl.training.performance.reports.domain.ResultPage;
import pl.training.performance.reports.ports.DataProvider;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Transactional(propagation = MANDATORY)
@Repository
@RequiredArgsConstructor
public class SpringDataJpaAdapter implements DataProvider {

    private final SpringDataJpaProvider dataProvider;
    private final SpringDataJpaMappper dataMapper;
    private DataChangeDelegate dataChangeDelegate = () -> {};

    @Override
    public ResultPage<DataEntry> findAll(PageSpec pageSpec) {
        var pageRequest = PageRequest.of(pageSpec.pageNumber(), pageSpec.pageSize());
        var page = dataProvider.findAll(pageRequest);
        return dataMapper.toDomain(page);
    }

    @Override
    public void add(DataEntry dataEntry) {
       var entity = dataMapper.toEntity(dataEntry);
       dataProvider.save(entity);
       dataChangeDelegate.dataChanged();
    }

    @Override
    public void setDelegate(DataChangeDelegate dataChangeDelegate) {
        this.dataChangeDelegate = dataChangeDelegate;
    }

}
