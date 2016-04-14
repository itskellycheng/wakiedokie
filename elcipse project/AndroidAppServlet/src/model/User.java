package model;

public class User {
    private int id;
    private String facebook_id;
    private String first_name;
    private String last_name;

    public User(String facebook_id, String first_name, String last_name) {
        this.facebook_id = facebook_id;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public int getId() {
        return id;
    }

    public String getFacebookId() {
        return facebook_id;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }
}
