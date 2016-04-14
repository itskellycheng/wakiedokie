package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;

public class DatabaseIO {
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Asiagodtonegg3be0*";

    /* isConnected */
    public boolean isConnected() {
        try {
            Connection myConnection = DriverManager.getConnection(URL, USERNAME,
                    PASSWORD);
            if (myConnection != null) {
                System.out.println("Connected to Database Successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /* display users in database */
    public void displayAllUsers() {
        // 1. Get Database connection
        try {
            Connection myConnection = DriverManager.getConnection(URL, USERNAME,
                    PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "displayAllUsers: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            Statement myStatement = myConnection.createStatement();
            String query_command = "select * from wakiedokie.user";
            // 3. Execute SQL query
            ResultSet myResultSet = myStatement.executeQuery(query_command);

            // 4. Process the Result Set
            while (myResultSet.next()) {
                System.out.println(myResultSet.getString("id") + ", "
                        + myResultSet.getString("facebook_id") + ", "
                        + myResultSet.getString("first_name") + ", "
                        + myResultSet.getString("last_name"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /* insert a new user to Database */
    public void insertUserDb(User user) {
        // 1. Get Database connection
        try {
            Connection myConnection = DriverManager.getConnection(URL, USERNAME,
                    PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "insertUserDb: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            String query_command = "INSERT IGNORE INTO wakiedokie.user VALUES (0,?,?,?);";
            PreparedStatement myPreparedStatement = myConnection
                    .prepareStatement(query_command);
            myPreparedStatement.setString(1, user.getFacebookId());
            myPreparedStatement.setString(2, user.getFirstName());
            myPreparedStatement.setString(3, user.getLastName());
            // 3. Execute SQL query
            myPreparedStatement.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}