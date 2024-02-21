package pl.training.performance.concurrency.ex12_count_down_latch;

import java.util.concurrent.CountDownLatch;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        Meeting meeting = new Meeting(countDownLatch);
        new Thread(meeting).start();
        meeting.addParticipant("Jan");
        Thread.sleep(2_000);
        meeting.addParticipant("Marek");
        meeting.addParticipant("Anna");
        meeting.addParticipant("Michał");
        Thread.sleep(1_000);
        meeting.addParticipant("Adam");
    }

}
