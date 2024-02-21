package pl.training.performance.concurrency.ex11_semaphore;

import java.util.UUID;
import java.util.concurrent.Semaphore;

public class Application {

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        //BoundedSemaphore semaphore = new BoundedSemaphore(5);
        PrintingQueue printingQueue = new PrintingQueue(semaphore);
        for (int threadIndex = 0; threadIndex < THREADS_COUNT; threadIndex++) {
            new Thread(() -> printingQueue.print(UUID.randomUUID().toString())).start();
        }
    }

}
