package com.wakiedokie.waikiedokie.model;

/**
 * Created by chaovictorshin-deh on 4/26/16.
 */
public class Alarm {

    private User sender;
    private User receiver;
    private String time;
    private String type;


    public Alarm(User sender, User receiver, String time, String type) {
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
        this.type = type;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
