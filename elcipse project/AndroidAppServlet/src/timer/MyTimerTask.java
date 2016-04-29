package timer;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        System.out.println("Executing timer task on " + TimerMain
                .convertTimeToDateFormat(System.currentTimeMillis()));
    }

}
