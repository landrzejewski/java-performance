package pl.training.performance.concurrency.ex10_cache;

import java.util.Objects;

public class Index<Key> implements Comparable<Index<Key>> {

    private final Long created = System.nanoTime();
    private final Key key;
    private Long accessCounter = 1L;

    public Index(Key key) {
        this.key = key;
    }

    public Long getAccessCounter() {
        return accessCounter;
    }

    public Long getCreated() {
        return created;
    }

    public Key getKey() {
        return key;
    }

    public void increaseAccessCounter() {
        accessCounter++;
    }

    @Override
    public int compareTo(Index<Key> index) {
        int result = accessCounter.compareTo(index.accessCounter);
        return result != 0 ? result : index.created.compareTo(created);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index<?> index = (Index<?>) o;
        return Objects.equals(accessCounter, index.accessCounter) &&
                Objects.equals(created, index.created) &&
                Objects.equals(key, index.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessCounter, created, key);
    }

    public boolean hasKey(Key key) {
        return this.key.equals(key);
    }

}
