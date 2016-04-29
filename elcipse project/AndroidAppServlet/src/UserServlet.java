import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.Alarm;
import model.User;
import util.DatabaseIO;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getOutputStream().println("Hurray !! This Servlet Works");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Received POST request");
        User user = null; // current user
        Alarm alarm_I_set = null;
        Alarm alarm_I_received = null;
        JSONParser parser = new JSONParser();

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            /* report an error */ }

        // Receiving Request from client.
        try {
            // Getting info of current user
            Object obj = parser.parse(jb.toString());
            JSONObject obj2 = (JSONObject) obj;
            if (obj2.get("first_name") != null) {
                user = new User(obj2.get("facebook_id").toString(),
                        obj2.get("first_name").toString(),
                        obj2.get("last_name").toString());
            }
            DatabaseIO dbio = new DatabaseIO();
            if (!dbio.isConnected()) {
                System.out.println("Cannot Connect to Server");
            }

            /*
             * if user is null : currUser is sending request other than the
             * periodic request
             */
            if (user != null) {
                // insert user if not existing
                dbio.insertUserDb(user);

                // check status of alarms i set
                alarm_I_set = dbio.getAlarmISetStatus(user.getFacebookId());
                // check if I received new request from others
                alarm_I_received = dbio
                        .checkForNewAlarmRequests(user.getFacebookId());
                // System.out.println(alarm_I_received.getOwner());
            }

            /* if currUser is responding to a request from an alarm request */
            // check current user's response
            if (obj2.get("I_accepted_new_alarm") != null) {
                boolean sanityCheck = alarmAcceptOrNotUpdateDB(obj2);
            }

            // show all users in current database
            // dbio.displayAllUsers();

        } catch (ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }

        // Sending Response from server
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JSONObject responseObj = new JSONObject();

            sendAlarmStatusCheck(responseObj, alarm_I_set);
            sendNewRequestCheck(responseObj, alarm_I_received);
            out.print(responseObj);

        } catch (IOException e) {

            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
                response.getWriter().close();
            } catch (IOException ioe) {
                /* ignore */
            }

        }
    }

    @SuppressWarnings({ "unchecked" })
    private void sendAlarmStatusCheck(JSONObject responseObj, Alarm alarm) {
        if (alarm == null) {
            responseObj.put("alarm", "null");
            return;
        }
        if (alarm.getStatus().equals("approved")) {
            responseObj.put("alarm", "approved");
        } else if (alarm.getStatus().equals("denied")) {
            responseObj.put("alarm", "denied");
        } else if (alarm.getStatus().equals("true")) {
            responseObj.put("alarm", "true");
        } else if (alarm.getStatus().equals("false")) {
            responseObj.put("alarm", "false");
        }

        responseObj.put("time", alarm.getTime());
        responseObj.put("user2_name", alarm.getUser2Name());

    }

    /**
     * For checking if anyone has send me a request.
     */
    @SuppressWarnings("unchecked")
    private void sendNewRequestCheck(JSONObject responseObj, Alarm alarm) {
        if (alarm == null) {
            responseObj.put("new_request_from_others", "null");
            return;
        }

        if (alarm.getStatus().equals("false")) {
            responseObj.put("new_request_from_others", "true");
            responseObj.put("new_request_from_others_time", alarm.getTime());
            System.out.println(alarm.getTime());
            responseObj.put("new_request_from_others_owner_fb_id",
                    alarm.getOwner());
            responseObj.put("new_request_from_others_owner_name",
                    alarm.getOwnerName());
            // System.out.println(alarm.getOwner());
        }

    }

    private boolean alarmAcceptOrNotUpdateDB(JSONObject obj) {
        DatabaseIO dbio = new DatabaseIO();
        String owner_fb_id = obj.get("owner_fb_id").toString();
        String my_fb_id = obj.get("my_fb_id").toString();
        if (obj.get("I_accepted_new_alarm").toString().equals("approved")) {
            dbio.updateAlarmStatus(owner_fb_id, my_fb_id, "approved");
            return true;

        } else if (obj.get("I_accepted_new_alarm").toString()
                .equals("denied")) {
            dbio.updateAlarmStatus(owner_fb_id, my_fb_id, "denied");
            return false;
        }
        // should not reach this line.
        return false;

    }

}
