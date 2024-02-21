package pl.training.performance.reports.provider;

import pl.training.performance.reports.DataEntry;
import pl.training.performance.reports.PageSpec;
import pl.training.performance.reports.ResultPage;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SynchronizedDataProvider implements DataProvider, AutoCloseable {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final DataProvider dataProvider;

    public SynchronizedDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public ResultPage<DataEntry> findAll(PageSpec pageSpec) {
        lock.readLock().lock();
        try {
            return dataProvider.findAll(pageSpec);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void add(DataEntry dataEntry) {
        lock.writeLock().lock();
        try {
            dataProvider.add(dataEntry);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void close() {
        if (dataProvider instanceof AutoCloseable provider) {
            try {
                provider.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
