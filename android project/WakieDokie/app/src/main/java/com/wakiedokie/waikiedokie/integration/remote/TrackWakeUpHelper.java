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
import java.util.TimerTask;

/**
 * Created by chaovictorshin-deh on 5/2/16.
 */
public class TrackWakeUpHelper extends TimerTask implements Response.Listener,
        Response.ErrorListener {

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = "http://10.0.0.25:8080/AndroidAppServlet/TrackWakeUpStatusServlet";

    private RequestQueue mQueue;
    private Activity activity;
    private DBHelper dbHelper;
    private String owner_fb_id;
    private String user2_fb_id;
    private int alarmID;
    private MediaPlayer mMediaPlayer;
    private Timer timer;

    public TrackWakeUpHelper(Activity activity, int alarmID, MediaPlayer mMediaPlayer, Timer timer) {
        this.activity = activity;
        dbHelper = new DBHelper(activity);
        this.alarmID = alarmID;
        owner_fb_id = dbHelper.getOwnerFbId(alarmID);
        user2_fb_id = dbHelper.getUser2FbId(alarmID);
        this.mMediaPlayer = mMediaPlayer;
        this.timer = timer;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {
        String bothAwake;
        try {
            bothAwake = ((JSONObject) response).getString("track_wakeup_status_bothAwake");
            if (bothAwake.equals("true")) {
                System.out.println("In TrackWakeUpHelper: Both awake!!!");
                mMediaPlayer.stop();
                timer.cancel();
                dbHelper.deleteAlarm(alarmID);
                Intent intent = new Intent(activity, AlarmMainActivity.class);
                activity.startActivity(intent);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("sending request to server from Tracking ");
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext())
                .getRequestQueue();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("track_wakeup_status_owner_fb_id", owner_fb_id);
            mJSONObject.put("track_wakeup_status_user2_fb_id", user2_fb_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);

    }
}
