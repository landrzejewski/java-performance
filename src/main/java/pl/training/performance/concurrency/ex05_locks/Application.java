package pl.training.performance.concurrency.ex05_locks;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Application {

    private static final int THREADS_COUNT = 2;
    private static final int PRINTING_QUEUE_LIMIT = 10;

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition queueIsFull = lock.newCondition();
        Condition queueIdEmpty = lock.newCondition();

        Queue<String> printingQueue = new LinkedList<>();
        new Thread(new Printer(printingQueue, lock, queueIsFull, queueIdEmpty)).start();
        for (int threadIndex = 0; threadIndex < THREADS_COUNT; threadIndex++) {
            new Thread(new Employee(printingQueue, PRINTING_QUEUE_LIMIT, lock, queueIsFull, queueIdEmpty)).start();
        }
    }

}
