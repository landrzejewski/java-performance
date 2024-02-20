package pl.training.performance.repoprts;

public interface DataProvider {

    ResultPage<DataEntry> findAll(PageSpec pageSpec);

}
