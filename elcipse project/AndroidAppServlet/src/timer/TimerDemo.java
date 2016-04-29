package timer;

import java.util.Timer;
import java.util.TimerTask;

public class TimerDemo {
    public static void main(String[] args) {
        // creating timer task, timer
        TimerTask tasknew = new MyTimerTask();
        Timer timer = new Timer();

        // scheduling the task at fixed rate delay
        timer.scheduleAtFixedRate(tasknew, 500, 1000);
    }

    // this method performs the task
    public void run() {
        System.out.println("working at fixed rate delay");
    }
}