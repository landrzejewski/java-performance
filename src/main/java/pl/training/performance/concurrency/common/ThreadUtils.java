package pl.training.performance.concurrency.common;

import lombok.extern.java.Log;

@Log
public class ThreadUtils {

    public static Thread asyncRun(Task task) {
        return new Thread(() -> {
            try {
                task.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
