package pl.training.performance.concurrency.ex06_read_write_lock;

import java.util.Random;
import java.util.UUID;

public class Application {

    private static final int THREADS_COUNT = 10;
    private static final KeyValueStore<Integer, String> KEY_VALUE_STORE = new KeyValueStore<>();

    public static void main(String[] args) {
        for (int threadIndex = 0; threadIndex < THREADS_COUNT; threadIndex++) {
            new Thread(Application::work).start();
        }
    }

    private static void work() {
        Random random = new Random();
        while (true) {
            if (random.nextBoolean()) {
                String value = UUID.randomUUID().toString();
                KEY_VALUE_STORE.put(random.nextInt(100), value);
                System.out.printf("Writing to store: %s\n", value);
            } else {
                System.out.printf("Reading from store: %s\n", KEY_VALUE_STORE.get(random.nextInt(100)));
            }
        }
    }

}
