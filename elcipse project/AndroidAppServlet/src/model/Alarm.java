package model;

/**
 * Alarm model.
 * 
 * @author chaovictorshin-deh
 *
 */
public class Alarm {
    private String owner;
    private String user2;
    private String time;
    private String type;
    private String status;
    private int id;

    private String ownerName = "Nope";
    private String user2Name;

    public Alarm(int id, String owner, String user2, String time,
            String status) {
        this.id = id;
        this.owner = owner;
        this.user2 = user2;
        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getUser2Name() {
        return user2Name;
    }

    public String getUser2() {
        return user2;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOwnerName(String name) {
        ownerName = name;
    }

    public void setUser2Name(String name) {
        user2Name = name;
    }

}
