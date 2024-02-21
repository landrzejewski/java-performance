package pl.training.performance.concurrency.ex16_promises;

import java.util.concurrent.Callable;

public class Sum implements Callable<Integer> {

    private static final int SLEEP_TIME = 3_000;

    private final int firstValue;
    private final int secondValue;

    public Sum(int firstValue, int secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    @Override
    public Integer call() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        return firstValue + secondValue;
    }

}
