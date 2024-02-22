package pl.training.performance.reports.ports;

import pl.training.performance.reports.domain.DataEntry;
import pl.training.performance.reports.domain.PageSpec;
import pl.training.performance.reports.domain.ResultPage;

public interface DataProvider {

    ResultPage<DataEntry> findAll(PageSpec pageSpec);

    default void add(DataEntry dataEntry) {
        throw new UnsupportedOperationException();
    }

    default void setDelegate(DataChangeDelegate dataChangeDelegate) {
    }

    interface DataChangeDelegate {

        void dataChanged();

    }

}
