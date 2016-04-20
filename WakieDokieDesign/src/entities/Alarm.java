package entities;

public class Alarm {
    private int id; // pk
    private User user1; // user who sent request
    private User user2; // user who accepted the request
    private String AlarmTypeUser1; // alarm type for user1 (set by user 2)
    private String AlarmTypeUser2; // alarm type for user2 (set by user 1)
    private boolean user1IsAwake; // true if user1 has pressed or completed the
                                  // wakeup task
    private boolean user2IsAwake; // true if user2 has pressed or completed the
                                  // wakeup task
    private String set_time; // the alarm time

}
