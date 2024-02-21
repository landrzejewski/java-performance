package pl.training.performance.concurrency.ex03_stopping_threads;

import java.util.Optional;
import java.util.Queue;

public class Processor implements Runnable {

    private static final int SLEEP_TIME = 3_000;

    private final Queue<Long> values;

    public Processor(Queue<Long> values) {
        this.values = values;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !values.isEmpty()) {
                Thread.sleep(SLEEP_TIME);
                Optional.ofNullable(values.poll())
                        .ifPresent(this::processValue);
            }
        } catch (InterruptedException e) {
            System.out.println("Stopping processing thread");
        }
        finally {
            System.out.println("Saving remaining values"); // bezpieczne zachowanie danych w momencie przerwania pracy wątki
        }
    }

    private void processValue(long value) {
        System.out.printf("Result for value %d: %s\n", value, value % 2 == 0);
    }

}
