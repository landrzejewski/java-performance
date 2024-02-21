package pl.training.performance.concurrency.ex05_locks;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Printer implements Runnable {

    private static final int MAX_SLEEP_TIME = 200;

    private final Queue<String> printingQueue;
    private final Lock lock;
    private final Condition queueIsFull;
    private final Condition queueIsEmpty;
    private final Random random = new Random();

    public Printer(Queue<String> printingQueue, Lock lock, Condition queueIsFull, Condition queueIsEmpty) {
        this.printingQueue = printingQueue;
        this.lock = lock;
        this.queueIsFull = queueIsFull;
        this.queueIsEmpty = queueIsEmpty;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            waitIfQueueIsEmpty();
            printDocument();
            lock.unlock();
        }
    }

    private void printDocument() {
        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_TIME));
        } catch (InterruptedException e) {
            System.out.println("Printing was interrupted...");
        }
        String text = printingQueue.poll();
        System.out.printf("Printing: %s\n", text);
        queueIsFull.signalAll();
    }

    private void waitIfQueueIsEmpty() {
        while (printingQueue.isEmpty()) {
            try {
                System.out.println("Queue is empty...");
                queueIsEmpty.await();
            } catch (InterruptedException e) {
                System.out.println("Printing was interrupted...");
            }
        }
    }

}
