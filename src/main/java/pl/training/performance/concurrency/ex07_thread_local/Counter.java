package pl.training.performance.concurrency.ex07_thread_local;

import java.util.Random;

public class Counter implements Runnable {

    private static final int MAX_SLEEP_TIME = 200;
    private static final long COUNTER_DEFAULT_VALUE = 1;

    private final Random random = new Random();
    private final ThreadLocal<Long> counter = new ThreadLocal<>();

    @Override
    public void run() {
        counter.set(COUNTER_DEFAULT_VALUE);
        while (true) {
            try {
                Thread.sleep(random.nextInt(MAX_SLEEP_TIME));
                counter.set(counter.get() + 1);
                System.out.printf("Current thread: %s, counter value: %d\n", Thread.currentThread().getName(), counter.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
