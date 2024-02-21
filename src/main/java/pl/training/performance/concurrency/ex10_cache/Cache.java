package pl.training.performance.concurrency.ex10_cache;

import java.util.*;

public class Cache<Key, Value> {

    private final Map<Key, Value> store = new HashMap<>();
    private final TreeSet<Index<Key>> indices = new TreeSet<>();
    private final long maxCapacity;

    public Cache(long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void put(Key key, Value value) {
       Optional<Index<Key>> index = getIndexByKey(key);
       if (index.isEmpty()) {
           indices.add(new Index<>(key));
       }
       store.put(key, value);
       ensureCapacity();
    }

    public synchronized Optional<Value> get(Key key) {
        getIndexByKey(key).ifPresent(Index::increaseAccessCounter);
        return Optional.ofNullable(store.get(key));
    }

    public synchronized List<Value> getAll() {
        return new ArrayList<>(store.values());
    }

    private void ensureCapacity() {
        if (indices.size() > maxCapacity) {
            Index<Key> index = indices.pollLast();
            if (index != null) {
                store.remove(index.getKey());
            }
        }
    }

    private Optional<Index<Key>> getIndexByKey(Key key) {
        return indices.stream()
                .filter(index -> index.hasKey(key))
                .findFirst();
    }

}
