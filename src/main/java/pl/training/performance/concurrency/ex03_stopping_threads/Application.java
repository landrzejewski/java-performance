package pl.training.performance.concurrency.ex03_stopping_threads;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));

        Queue<Long> values = new LinkedList<>(List.of(1L, 2L, 3L, 4L, 5L, 6L));
        Processor processor = new Processor(values);
        Thread thread = new Thread(processor);
        thread.start();
         //thread.stop(); // nie używać - może doprowadzić do niespójności stanu
        thread.join(4_000);
        thread.interrupt();
    }

}
