package com.wakiedokie.waikiedokie.model;

import java.util.ArrayList;

/**
 * Created by chaovictorshin-deh on 4/26/16.
 */
public class LocalStorage {
    private User me;
    private ArrayList<Alarm> Alarms;

    public LocalStorage() {

    }

    public LocalStorage(User me, ArrayList<Alarm> Alarms) {
        this.me = me;
        this.Alarms = Alarms;
    }

    public void setUser(User me) {
        me = me;
    }

    public User getCurrentUser() {
        return me;
    }

    public String userToString() {
        return "Facebook id:" + me.getFacebookId() + ". First name:" + me.getFirstName();
    }

}
