package com.wakiedokie.waikiedokie.util.timer;

import android.app.Activity;
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
    private static final String SERVER_URL = "http://128.237.191.203:8080/AndroidAppServlet/UserServlet";
    private RequestQueue mQueue;
    private User user;
    private Activity activity;


    String my_fb_id;
    public MyTimerTask(User user, Activity activity) {
        super();
        this.user = user;
        this.activity = activity;
    }

    public MyTimerTask(Activity activity) {
        this.activity = activity;
        DBHelper mydb = new DBHelper(activity);
        Cursor result = mydb.getData(1);
        result.moveToFirst();
        my_fb_id = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_FACEBOOK_ID));
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
            System.out.println(user.getFacebookId());
            System.out.println("JSON object ready to be sent");

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

        // should check the the status of alarm table
        // for alarm I set
        String user2;
        String title = "";
        String message;
        String owner_fb_id;
        String owner_name;
        String time;

        try {
            // 1. check the status of alarm I set before
            String alarm = ((JSONObject) response).getString("alarm");
            if (alarm.equals("approved")) {
                user2 = ((JSONObject) response).getString("user2_name");
                time = ((JSONObject) response).getString("time");
                title = "Request approved!";
                message = user2 + " has become your wakie buddy!!" + "Time:" + time;
                Message.showAlert(activity, title, message);

            } else if (alarm.equals("denied")) {
                user2 = ((JSONObject) response).getString("user2_name");
                time = ((JSONObject) response).getString("time");
                title = "Request denied";
                message = user2 + " has declined your request.." + "Time:" + time;
                Message.showAlert(activity, title, message);
            }

            // 2. check if I received a new alarm request
            String received_new_alarm = ((JSONObject) response).getString("new_request_from_others");
            if (received_new_alarm != null && received_new_alarm.equals("true")) {
                owner_fb_id = ((JSONObject) response).getString("new_request_from_others_owner_fb_id");
                owner_name = ((JSONObject) response).getString("new_request_from_others_owner_name");
                time =  ((JSONObject) response).getString("new_request_from_others_time");
                title = "You Received a New Request!";
                message = "New Request from: " + owner_name + "Time:"+ time;
                Message.showAlertRequestFromOthers(activity, owner_name, owner_fb_id, title, message);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(activity, "SUCCESS: Connection to server is good", Toast.LENGTH_SHORT).show();

    }



    public void acceptRequest(Activity activity, String owner_name, String owner_fb_id) {
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

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    public void denyRequest(Activity activity, String owner_name, String owner_fb_id) {
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
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }
}
