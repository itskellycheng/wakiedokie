package timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class TimerMain {

    public static void main(String[] args) {
        System.out.println("Timer tasks started on "
                + convertTimeToDateFormat(System.currentTimeMillis()));
        System.out.println("**************************");
        // It will create new thread
        Timer timer = new Timer();

        timer.schedule(new MyTimerTask(), 1000, 1000);
        // Stopping the timer thread after some time for example :12 secs.

        // try {
        // Thread.sleep(5000);
        // } catch (InterruptedException e) {
        //
        // e.printStackTrace();
        // }

        // you can call timer.cancel() or System.exit(0)
        System.out.println("**************************");
        System.out.println("Timer tasks finished on "
                + convertTimeToDateFormat(System.currentTimeMillis()));

        System.exit(0);
    }

    public static String convertTimeToDateFormat(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultdate = new Date(milliseconds);
        return sdf.format(resultdate);
    }

}
