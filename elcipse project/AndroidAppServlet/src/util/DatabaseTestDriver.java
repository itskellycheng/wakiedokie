package util;

public class DatabaseTestDriver {

    public static void main(String[] args) {
        DatabaseIO dbIO = new DatabaseIO();
        // System.out.println("Connection: " + dbIO.isConnected());
        dbIO.displayAllUsers();
    }
}
