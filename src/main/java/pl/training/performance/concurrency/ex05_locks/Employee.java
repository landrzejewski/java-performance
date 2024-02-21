package pl.training.performance.concurrency.ex05_locks;

import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Employee implements Runnable {

    private static final int MAX_SLEEP_TIME = 300;

    private final Queue<String> printingQueue;
    private final Lock lock;
    private final Condition queueIsFull;
    private final Condition queueIsEmpty;
    private final int tasksLimit;
    private final Random random = new Random();

    public Employee(Queue<String> printingQueue, int tasksLimit, Lock lock, Condition queueIsFull, Condition queueIsEmpty) {
        this.printingQueue = printingQueue;
        this.tasksLimit = tasksLimit;
        this.lock = lock;
        this.queueIsFull = queueIsFull;
        this.queueIsEmpty = queueIsEmpty;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            waitIfQueueIsFull();
            addDocument();
            lock.unlock();
        }
    }

    private void addDocument() {
        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_TIME));
        } catch (InterruptedException e) {
            System.out.println("Employee was interrupted...");
        }
        String text = UUID.randomUUID().toString();
        printingQueue.add(text);
        System.out.printf("New document added to printing queue: %s\n", text);
        queueIsEmpty.signalAll();
    }

    private void waitIfQueueIsFull() {
        while (tasksLimit == printingQueue.size()) {
            try {
                System.out.println("Queue limit reached...");
                queueIsFull.await();
            } catch (InterruptedException e) {
                System.out.println("Employee was interrupted...");
            }
        }
    }

}
