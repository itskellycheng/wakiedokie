package util;

import model.Alarm;

public class DatabaseTestDriver {

    public static void main(String[] args) {
        String my_fb_id = "1151451178206737";
        String other_fb_id = "10209500772462847";
        DatabaseIO dbIO = new DatabaseIO();
        // System.out.println("Connection: " + dbIO.isConnected());
        dbIO.displayAllUsers();
        Alarm alarm = dbIO.getAlarmISetStatus(my_fb_id);
        if (alarm == null)
            System.out.println("Do nothing");

        Alarm alarm_requested = dbIO.checkForNewAlarmRequests(other_fb_id);
        System.out.println(alarm_requested.getOwner());
        dbIO.updateAlarmStatus(other_fb_id, my_fb_id, "false");

    }
}
