package util;

/**
 * Test driver for database in/out.
 * 
 * @author chaovictorshin-deh
 *
 */
public class DatabaseTestDriver {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        String my_fb_id = "1151451178206737";
        String other_fb_id = "10209500772462847";
        DatabaseIO dbIO = new DatabaseIO();
        System.out.println(dbIO.bothAreAwake(my_fb_id, other_fb_id));
        dbIO.deleteAlarm(my_fb_id, other_fb_id);
        // int id = dbIO.insertAlarmDb(my_fb_id, other_fb_id,
        // "123123123123123123");
        // System.out.println(id);
        // System.out.println("Connection: " + dbIO.isConnected());
        // dbIO.displayAllUsers();
        // Alarm alarm = dbIO.getAlarmISetStatus(my_fb_id);
        // System.out.println(alarm.getId());
        // if (alarm == null)
        // System.out.println("Do nothing");

        // ArrayList<User> users = dbIO.getUsers("10209500772462847");
        // // for (int i = 0; i < users.size(); i++) {
        // // System.out.println(users.get(i).toString());
        // // }
        // JSONArray jsonArray = new JSONArray();
        // for (int i = 0; i < users.size(); i++) {
        // jsonArray.add(users.get(i).getJSONObject());
        // }
        // System.out.println(jsonArray.toString());

        //
        // Alarm alarm_requested = dbIO.checkForNewAlarmRequests(other_fb_id);
        // System.out.println(alarm_requested.getId());
        // System.out.println(alarm_requested.getOwner());
        // dbIO.updateAlarmStatus(other_fb_id, my_fb_id, "false");

    }
}
