package pl.training.performance.reports.common;

import java.util.*;

public class LinkedHashMapCache<Key, Value> {

    private Map<Key, Value> store;

    public LinkedHashMapCache(int maxCapacity) {
        store = Collections.synchronizedMap(new LinkedHashMap<>(maxCapacity, 1, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<Key, Value> eldest) {
                return size() > maxCapacity;
            }

        });
    }

    public Optional<Value> get(Key key) {
        return Optional.ofNullable(store.get(key));
    }

    public void put(Key key, Value value) {
        store.put(key, value);
    }

    public void clear() {
        System.out.println("Clearing cache...");
        store.clear();
    }

}
