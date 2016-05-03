package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Alarm;
import model.User;

public class DatabaseIO {
//    private static final String URL = "jdbc:mysql://localhost:3306";
//    private static final String USERNAME = "root";
//    private static final String PASSWORD = "Asiagodtonegg3be0*";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306";
    private static final String USERNAME = "kelly";
    private static final String PASSWORD = "kelly";


    /* isConnected */
    public boolean isConnected() {
        Connection myConnection = null;
        try {
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (myConnection != null) {
                System.out.println("Connected to Database Successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(myConnection);
        }
    }

    /* display users in database */
    public void displayAllUsers() {
        // 1. Get Database connection
        Connection myConnection = null;
        Statement myStatement = null;
        ResultSet myResultSet = null;
        try {
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "displayAllUsers: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            myStatement = myConnection.createStatement();
            String query_command = "select * from wakiedokie.user";
            // 3. Execute SQL query
            myResultSet = myStatement.executeQuery(query_command);

            // 4. Process the Result Set
            while (myResultSet.next()) {
                System.out.println(myResultSet.getString("facebook_id") + ", "
                        + myResultSet.getString("first_name") + ", "
                        + myResultSet.getString("last_name"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(myConnection);
            closeResultSet(myResultSet);
            closeStatement(myStatement);
        }

    }

    /* insert a new user to Database */
    public void insertUserDb(User user) {
        Connection myConnection = null;
        PreparedStatement myPreparedStatement = null;
        // 1. Get Database connection
        try {
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "insertUserDb: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            String query_command = "INSERT IGNORE INTO wakiedokie.user VALUES (?,?,?);";
            myPreparedStatement = myConnection.prepareStatement(query_command);
            myPreparedStatement.setString(1, user.getFacebookId());
            myPreparedStatement.setString(2, user.getFirstName());
            myPreparedStatement.setString(3, user.getLastName());
            // 3. Execute SQL query
            myPreparedStatement.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(myConnection);
            closePreparedStatement(myPreparedStatement);
        }

    }

    /* insert a new alarm to Database */
    public void insertAlarmDb(String user1_facebook_id,
            String user2_facebook_id, String time) {
        Connection myConnection = null;
        PreparedStatement myPreparedStatement = null;
        // 1. Get Database connection
        try {
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "insertAlarmDb: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            String query_command = "INSERT IGNORE INTO wakiedokie.alarm (time, user_facebook_id, user_facebook_id1) VALUES (?,?,?);";
            myPreparedStatement = myConnection.prepareStatement(query_command);
            System.out.println(time);
            myPreparedStatement.setString(1, time);
            myPreparedStatement.setString(2, user1_facebook_id);
            myPreparedStatement.setString(3, user2_facebook_id);
            // 3. Execute SQL query
            myPreparedStatement.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(myConnection);
            closePreparedStatement(myPreparedStatement);
        }

    }

    /* update alarm status */
    public void updateAlarmStatus(String owner_facbook_id,
            String user2_facebook_id, String status) {
        Connection myConnection = null;
        PreparedStatement myPreparedStatement = null;
        String user1 = owner_facbook_id;
        String user2 = user2_facebook_id;

        try {
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "updateAlarmStatus: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            String query_command = "UPDATE wakiedokie.alarm SET status = ?  WHERE user_facebook_id = ? AND user_facebook_id1 = ?;";
            myPreparedStatement = myConnection.prepareStatement(query_command);
            myPreparedStatement.setString(1, status);
            myPreparedStatement.setString(2, user1);
            myPreparedStatement.setString(3, user2);
            System.out.println("owner: " + user1);
            System.out.println("user2: " + user2);
            // 3. Execute SQL query
            myPreparedStatement.executeUpdate();
            System.out
                    .println("SUCCESS: updated alarm status to ---> " + status);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(myConnection);
            closeStatement(myPreparedStatement);
        }

    }

    /* display alarms in database */
    public void displayAllAlarms() {
        Connection myConnection = null;
        Statement myStatement = null;
        ResultSet myResultSet = null;
        // 1. Get Database connection
        try {
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "displayAllAlarms: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            myStatement = myConnection.createStatement();
            String query_command = "select * from wakiedokie.alarm";
            // 3. Execute SQL query
            myResultSet = myStatement.executeQuery(query_command);

            // 4. Process the Result Set
            while (myResultSet.next()) {
                System.out.println(myResultSet.getString("user_facebook_id")
                        + ", " + myResultSet.getString("user_facebook_id1"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(myConnection);
            closeStatement(myStatement);
            closeResultSet(myResultSet);
        }
    }

    /* get alarm status of a user in database */
    @SuppressWarnings("resource")
    public Alarm getAlarmISetStatus(String facebook_id) {

        Connection myConnection = null;
        PreparedStatement myPreparedStatement = null;
        ResultSet myResultSet = null;
        Alarm alarm = null;
        String owner = facebook_id;
        String user2 = "";
        String time = "";
        String status = "";
        String user2Name = "";
        // 1. Get Database connection
        try {
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "getAlarmStatus: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            String query_command = "SELECT * FROM wakiedokie.alarm WHERE user_facebook_id = ?;";
            // 3. Execute SQL query
            myPreparedStatement = myConnection.prepareStatement(query_command);
            myPreparedStatement.setString(1, facebook_id);
            myResultSet = myPreparedStatement.executeQuery();
            // 4. Process the Result Set

            // no matches in database
            if (!myResultSet.next()) {
                return null;
            }

            owner = myResultSet.getString("user_facebook_id");
            user2 = myResultSet.getString("user_facebook_id1");
            time = myResultSet.getString("time");
            status = myResultSet.getString("status");

            if (status.equals("denied")) {
                // Denied by user2: delete alarm row
                query_command = "DELETE FROM wakiedokie.alarm WHERE user_facebook_id = ? AND user_facebook_id1 = ?;";
                myPreparedStatement = myConnection
                        .prepareStatement(query_command);
                myPreparedStatement.setString(1, owner);
                myPreparedStatement.setString(2, user2);
                System.out.println("Alarm startus: alarm denied");
                System.out.println("Delete row in alarm table");
                myPreparedStatement.executeUpdate();

            } else if (status.equals("approved")) {
                // Approved by user2: update to status to 'true';
                query_command = "UPDATE wakiedokie.alarm SET status = ?  WHERE user_facebook_id = ? AND user_facebook_id1 = ?;";
                myPreparedStatement = myConnection
                        .prepareStatement(query_command);
                myPreparedStatement.setString(1, "true");
                myPreparedStatement.setString(2, owner);
                myPreparedStatement.setString(3, user2);
                myPreparedStatement.executeUpdate();
                System.out.println("Alarm status: alarm approved");
                myPreparedStatement.executeUpdate();
            } else if (status.equals("true")) {
                System.out.println("Alarm status: alarm is now activate");
            } else {
                System.out.println(
                        "Alarm status: alarm is still waitng to be approved");
            }
            System.out
                    .println(time + ", user 1:" + owner + ", user 2:" + user2);

            alarm = new Alarm(owner, user2, time, status);

            // Get name of user2
            query_command = "SELECT DISTINCT first_name FROM wakiedokie.alarm as a, wakiedokie.user as u where u.facebook_id = ?;";
            myPreparedStatement = myConnection.prepareStatement(query_command);
            myPreparedStatement.setString(1, user2);
            myResultSet = myPreparedStatement.executeQuery();

            if (myResultSet.next()) {
                user2Name = myResultSet.getString("first_name");
                alarm.setUser2Name(user2Name);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(myConnection);
            closeResultSet(myResultSet);
            closePreparedStatement(myPreparedStatement);
        }

        return alarm;

    }

    /**
     * check for new alarm request from other users with current user's Facebook
     * id.
     * 
     * @param facebook_id
     * @return
     */
    @SuppressWarnings("resource")
    public Alarm checkForNewAlarmRequests(String facebook_id) {
        Connection myConnection = null;
        PreparedStatement myPreparedStatement = null;
        ResultSet myResultSet = null;
        Alarm alarm = null;
        String currUser = facebook_id;
        String owner = "";
        String time = "";
        String status = "";
        String ownerName = "";

        // 1. Get Database connection
        try {
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (myConnection != null) {
                System.out.println(
                        "checkForNewAlarmRequests: Connected to Database Successfully");
            }

            // 2. Create a statement and Query command
            String query_command = "SELECT * FROM wakiedokie.alarm WHERE user_facebook_id1 = ?;";
            // 3. Execute SQL query
            myPreparedStatement = myConnection.prepareStatement(query_command);
            myPreparedStatement.setString(1, facebook_id);
            myResultSet = myPreparedStatement.executeQuery();

            // 4. Process the Result Set

            // no matches in database
            if (!myResultSet.next()) {
                System.out.println("No new alarm request");
                return null;
            }

            owner = myResultSet.getString("user_facebook_id");
            currUser = myResultSet.getString("user_facebook_id1");
            time = myResultSet.getString("time");
            status = myResultSet.getString("status");

            if (!status.equals("false")) {
                System.out.println("No new alarm request");
                return null;
            }

            // There is a new request to me. prepare info from db.
            query_command = "SELECT * FROM wakiedokie.alarm WHERE user_facebook_id1 = ? AND status = ?;";
            myPreparedStatement = myConnection.prepareStatement(query_command);
            myPreparedStatement.setString(1, currUser);
            myPreparedStatement.setString(2, "false");
            myResultSet = myPreparedStatement.executeQuery();

            // Get name of owner.
            query_command = "SELECT DISTINCT first_name FROM wakiedokie.alarm as a, wakiedokie.user as u where u.facebook_id = ?;";
            myPreparedStatement = myConnection.prepareStatement(query_command);
            myPreparedStatement.setString(1, owner);
            myResultSet = myPreparedStatement.executeQuery();

            alarm = new Alarm(owner, currUser, time, status);

            if (myResultSet.next()) {
                ownerName = myResultSet.getString("first_name");
                alarm.setOwnerName(ownerName);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(myConnection);
            closeResultSet(myResultSet);
            closePreparedStatement(myPreparedStatement);
        }
        return alarm;
    }

    private void closeConnection(Connection connection) {

        try {
            connection.close();
        } catch (SQLException e) {

        }
    }

    private void closeResultSet(ResultSet resultSet) {

        try {
            resultSet.close();
        } catch (SQLException e) {

        }
    }

    private void closePreparedStatement(PreparedStatement ps) {

        try {
            ps.close();
        } catch (SQLException e) {

        }
    }

    private void closeStatement(Statement s) {
        try {
            s.close();
        } catch (SQLException e) {

        }
    }

}