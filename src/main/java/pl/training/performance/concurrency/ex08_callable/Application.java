package pl.training.performance.concurrency.ex08_callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Application {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Sum sum = new Sum(1, 5);
        FutureTask<Integer> futureTask = new FutureTask<>(sum);
        new Thread(futureTask).start();
        while (!futureTask.isDone()) {
            System.out.println("Waiting...");
            Thread.sleep(2_000);
        }
        System.out.printf("Result: %d\n", futureTask.get());
    }

}
