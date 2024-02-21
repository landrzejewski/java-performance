package pl.training.performance.reports.provider;

import pl.training.performance.reports.DataEntry;
import pl.training.performance.reports.PageSpec;
import pl.training.performance.reports.ResultPage;

public interface DataProvider {

    ResultPage<DataEntry> findAll(PageSpec pageSpec);

    default void add(DataEntry dataEntry) {
        throw new UnsupportedOperationException();
    }

}
