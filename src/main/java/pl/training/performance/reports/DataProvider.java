package pl.training.performance.reports;

public interface DataProvider {

    ResultPage<DataEntry> findAll(PageSpec pageSpec);

}
