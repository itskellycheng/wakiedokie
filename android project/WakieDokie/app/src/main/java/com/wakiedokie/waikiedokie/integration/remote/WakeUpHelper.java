package com.wakiedokie.waikiedokie.integration.remote;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.ui.AlarmMainActivity;
import com.wakiedokie.waikiedokie.util.CustomJSONObjectRequest;
import com.wakiedokie.waikiedokie.util.CustomVolleyRequestQueue;
import com.wakiedokie.waikiedokie.database.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

/**
 * Created by chaovictorshin-deh on 5/2/16.
 */
public class WakeUpHelper implements Response.Listener,
        Response.ErrorListener {

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = "http://128.237.176.247:8081/AndroidAppServlet/WakeUpStatusServlet";
    private RequestQueue mQueue;
    private Activity activity;
    private DBHelper dbHelper;
    private String my_fb_id;
    private String owner_fb_id;
    private String user2_fb_id;
    private boolean imOwnerOfAlarm;
    private MediaPlayer mMediaPlayer;
    private int alarmID;

    public WakeUpHelper(Activity activity, int alarmID, MediaPlayer mMediaPlayer) {
        this.activity = activity;
        dbHelper = new DBHelper(activity);
        my_fb_id = dbHelper.getMyIDFromMeTable();
        this.alarmID = alarmID;
        // get owner_fb_id and user2_fb_id from database
        owner_fb_id = dbHelper.getOwnerFbId(alarmID);
        user2_fb_id = dbHelper.getUser2FbId(alarmID);
        imOwnerOfAlarm = dbHelper.imOwnerOfAlarm(alarmID, my_fb_id);
        this.mMediaPlayer = mMediaPlayer;
    }

    public void sendWakeUpMessageToServer() {
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext())
                .getRequestQueue();
        JSONObject mJSONObject = new JSONObject();


        try {
            mJSONObject.put("waking_up_owner_fb_id", owner_fb_id);
            mJSONObject.put("waking_up_user2_fb_id", user2_fb_id);

            if (imOwnerOfAlarm) {
                mJSONObject.put("waking_up_my_role", "owner");
            } else {
                mJSONObject.put("waking_up_my_role", "user2");
            }

            System.out.println("Sending request to sever: sendWakeUpMessageToServer");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("ERROR in WakeUpHelper response");
    }

    @Override
    public void onResponse(Object response) {
        String bothAwake = "";
        try {
            bothAwake = ((JSONObject) response).getString("bothAwake");
            if (bothAwake.equals("true")) {
                mMediaPlayer.stop();
                dbHelper.deleteAlarm(alarmID);
                Intent intent = new Intent(activity, AlarmMainActivity.class);
                activity.startActivity(intent);
            } else {
                System.out.println("starting timer task");
                Timer timer = new Timer();
                TrackWakeUpHelper twuHelper = new TrackWakeUpHelper(activity, alarmID, mMediaPlayer, timer);
                // scheduling the task at fixed rate delay
                timer.scheduleAtFixedRate(twuHelper, 500, 3000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
