package pl.training.performance.concurrency.ex16_promises;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        FutureTask<Integer> futureTask = new FutureTask<>(new Sum(2, 5));
        CompletableFuture//.runAsync(futureTask)
                .supplyAsync(() -> futureTask)
                .thenRunAsync(() -> System.out.println(Thread.currentThread().getName()), executorService)
                .thenRun(() -> System.out.println(Thread.currentThread().getName()))
                .thenAccept(System.out::println);

        Thread.sleep(1_0000);
    }

}
