package pl.training.performance.concurrency.ex03_stopping_threads;

public class ShutdownHook implements Runnable {

    @Override
    public void run() {
        System.out.println("Performing tasks before shutdown...");
    }

}
