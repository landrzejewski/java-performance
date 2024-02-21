package pl.training.performance.concurrency.ex02_joining_threads;

public class Sum implements Runnable {

    private static final int SLEEP_TIME = 3_000;

    private final int firstValue;
    private final int secondValue;

    public Sum(int firstValue, int secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(SLEEP_TIME);
            System.out.printf("Result: %s\n", firstValue + secondValue);
        } catch (InterruptedException e) {
            System.out.println("Stopping sum thread");
        }
    }

}
