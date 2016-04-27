package com.wakiedokie.waikiedokie.util.timer;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.dialogs.Message;
import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.util.CustomJSONObjectRequest;
import com.wakiedokie.waikiedokie.util.CustomVolleyRequestQueue;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimerTask;

/**
 * Created by chaovictorshin-deh on 4/25/16.
 */
public class MyTimerTask extends TimerTask implements Response.Listener,
        Response.ErrorListener{

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = "http://128.237.166.30:8080/AndroidAppServlet/UserServlet";
    private RequestQueue mQueue;
    private User user;
    private Activity activity;
    public MyTimerTask(User user, Activity activity) {
        super();
        this.user = user;
        this.activity = activity;
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
        String user2;
        String title;
        String message;
        try {
            String alarm = ((JSONObject) response).getString("alarm");
            if (alarm.equals("approved")) {
                user2 = ((JSONObject) response).getString("user2");
                title = "Request approved!";
                message = "Time:" + ",  With:";
                Message.showAlert(activity, title, message);

            } else if (alarm.equals("denied")) {
                user2 = ((JSONObject) response).getString("user2");
                title = "Request denied";
                message = "Time:" + ", With: ";
                Message.showAlert(activity, title, message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(activity, "SUCCESS: Connection to server is good", Toast.LENGTH_SHORT).show();

    }
}
