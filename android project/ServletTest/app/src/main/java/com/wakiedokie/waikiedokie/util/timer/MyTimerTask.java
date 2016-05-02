package com.wakiedokie.waikiedokie.util.timer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.dialogs.Message;
import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.util.CustomJSONObjectRequest;
import com.wakiedokie.waikiedokie.util.CustomVolleyRequestQueue;
import com.wakiedokie.waikiedokie.util.database.DBHelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimerTask;

/**
 * Created by chaovictorshin-deh on 4/25/16.
 */
public class MyTimerTask extends TimerTask implements Response.Listener,
        Response.ErrorListener{

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = "http://10.0.0.25:8080/AndroidAppServlet/UserServlet";
    private RequestQueue mQueue;
    private User user;
    private Activity activity;
    private DBHelper dbHelper;


    String my_fb_id;
    public MyTimerTask(User user, Activity activity) {
        super();
        this.user = user;
        this.activity = activity;
    }

    public MyTimerTask(Activity activity) {
        this.activity = activity;
        DBHelper mydb = new DBHelper(activity);
        my_fb_id = mydb.getMyFbId(1);
    }

    @Override
    public void run() {
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext())
                .getRequestQueue();
        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject.put("facebook_id", user.getFacebookId());
            mJSONObject.put("first_name", user.getFirstName());
            mJSONObject.put("last_name", user.getLastName());
            //System.out.println(user.getFacebookId());
            //System.out.println("JSON object ready to be sent");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    public String getFirstName(String firstName) {
        return firstName;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(activity, "Error Server Response", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object response) {
        dbHelper = new DBHelper(activity);
        // should check the the status of alarm table
        // for alarm I set
        String user2_name;
        String user2_fb_id;
        String my_fb_id = dbHelper.getMyFbId(1);;
        String title = "";
        String message;
        String owner_name;
        String time;
        String owner_fb_id;
        String alarm_id;

        try {
            // 1. check the status of alarm I set before
            String alarm = ((JSONObject) response).getString("alarm");
            if (alarm.equals("approved")) {
                alarm_id = ((JSONObject) response).getString("alarm_id");
                user2_name = ((JSONObject) response).getString("user2_name");
                user2_fb_id = ((JSONObject) response).getString("user2_fb_id");
                time = ((JSONObject) response).getString("time");
                title = "Request approved!";
                message = user2_name + " has become your wakie buddy!!" + "Time:" + time;
                System.out.println("Before setting server id");
                dbHelper.printAllAlarms();
                dbHelper.setAlarmServerId(my_fb_id, user2_fb_id, alarm_id);
                // 1. update this alarm with 2 fb_ids --> to active.
                dbHelper.setAlarmStatus(my_fb_id, user2_fb_id, DBHelper.ALARM_TYPE_NOT_SET);
                System.out.println("After setting server id");
                dbHelper.printAllAlarms();

                System.out.println("Check the status of alarm I set before");
                dbHelper.printAllAlarms();
                if(!activity.isFinishing())
                {
                    //show dialog
                    Message.showAlert(activity, title, message);
                }

                // 2. add pending intent



            } else if (alarm.equals("denied")) {
                alarm_id = ((JSONObject) response).getString("alarm_id");
                user2_name = ((JSONObject) response).getString("user2_name");
                user2_fb_id = ((JSONObject) response).getString("user2_fb_id");
                time = ((JSONObject) response).getString("time");
                title = "Request denied";
                message = user2_name + " has declined your request.." + "Time:" + time;
                // 1. delete this alarm from table
                dbHelper.deleteAlarm(my_fb_id, user2_fb_id);
                System.out.println("Deleted alarm from local alarm table");
                dbHelper.printAllAlarms();

                if(!activity.isFinishing())
                {
                    //show dialog
                    Message.showAlert(activity, title, message);
                }

            }


            // 2. check if I received a new alarm request
            String received_new_alarm = ((JSONObject) response).getString("new_request_from_others");
            if (received_new_alarm != null && received_new_alarm.equals("true")) {
                alarm_id = ((JSONObject) response).getString("alarm_id");
                System.out.println("alarm server ererererer id : " + alarm_id );
                owner_fb_id = ((JSONObject) response).getString("new_request_from_others_owner_fb_id");
                owner_name = ((JSONObject) response).getString("new_request_from_others_owner_name");
                time =  ((JSONObject) response).getString("new_request_from_others_time");
                title = "You Received a New Request!";
                message = "New Request from: " + owner_name + "Time:"+ time;

                if(!activity.isFinishing())
                {
                    //show dialog
                    Message.showAlertRequestFromOthers(activity, alarm_id, time, owner_name, owner_fb_id, title, message);
                }
                dbHelper.printAllAlarms();


            }

            // 3. check if an alarm was deleted by user2
            String deleter_name = ((JSONObject) response).getString("alarm_deleted_deleter_name");
//            System.out.println(deleter_name);
            if (deleter_name != null && !deleter_name.equals("null")) {
                System.out.println("deleted alarm");
                System.out.println(deleter_name + "has deleted your alarm");
                String d_owner_fb_id = ((JSONObject) response).getString("alarm_deleted_owner_fb_id");
                String d_user2_fb_id = ((JSONObject) response).getString("alarm_deleted_user2_fb_id");
                dbHelper.deleteAlarm(d_owner_fb_id, d_user2_fb_id);
                title = "Your wakie buddy has deleted the alarm!";
                message = deleter_name + " has deleted the alarm!";
                if(!activity.isFinishing())
                {
                    //show dialog
                    Message.showAlert(activity, title, message);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(activity, "SUCCESS: Connection to server is good", Toast.LENGTH_SHORT).show();

    }



    public void acceptRequest(Activity activity, String alarm_server_id, String time, String owner_name, String owner_fb_id) {
        dbHelper = new DBHelper(activity);
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext())
                .getRequestQueue();
        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject.put("I_accepted_new_alarm", "approved");
            mJSONObject.put("owner_fb_id", owner_fb_id);
            mJSONObject.put("my_fb_id", my_fb_id);
            System.out.println("I just accepted an request!");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 1. insert new alarm 2 fb_ids, time, active
        dbHelper.addAlarm(time, owner_fb_id, my_fb_id, DBHelper.ALARM_TYPE_NOT_SET);
        dbHelper.setAlarmServerId(owner_fb_id, my_fb_id, alarm_server_id);
        dbHelper.printAllAlarms();

        // print alarms debug use
        System.out.println("Accepted an alarm invitation. Should add a new alarm to alarm table");
        dbHelper.printAllAlarms();


        // 2, add pending intent


        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);

    }

    public void denyRequest(Activity activity, String owner_name, String owner_fb_id) {
        dbHelper = new DBHelper(activity);
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext())
                .getRequestQueue();
        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject.put("I_accepted_new_alarm", "denied");
            mJSONObject.put("owner_fb_id", owner_fb_id);
            mJSONObject.put("my_fb_id", my_fb_id);
            System.out.println("I just denied an request!");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // alarm table: do nothing
        System.out.println("Denied a request. Do nothing");

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }


    public void deleteAlarm(Activity activity, int alarmID) {
        if (alarmID == -1) {
            System.out.println("nothing to be deleted");
            return;
        }
        dbHelper = new DBHelper(activity);
        Cursor res = dbHelper.getAlarm(alarmID);
        System.out.println("trying to delete alarm id: " + alarmID);
        res.moveToFirst();
        String owner_fb_id = res.getString(res.getColumnIndex("owner_fb_id"));
        String user2_fb_id = res.getString(res.getColumnIndex("user2_fb_id"));
        dbHelper.deleteAlarm(alarmID);
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext()).getRequestQueue();

        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("deleted_alarm", "deleted");
            mJSONObject.put("my_fb_id", my_fb_id);
            mJSONObject.put("owner_fb_id", owner_fb_id);
            mJSONObject.put("user2_fb_id", user2_fb_id);
            System.out.println(my_fb_id + "has deleted an alarm");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }
}
