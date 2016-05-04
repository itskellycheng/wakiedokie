package com.wakiedokie.waikiedokie.integration.remote;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.database.DBHelper;
import com.wakiedokie.waikiedokie.ui.AlarmMainActivity;
import com.wakiedokie.waikiedokie.util.CustomJSONObjectRequest;
import com.wakiedokie.waikiedokie.util.CustomVolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

/**
 * Created by chaovictorshin-deh on 5/4/16.
 */
public class GetAlarmTypeHelper implements Response.Listener,
        Response.ErrorListener {
    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = Connection.GET_ALARM_TYPE_SERVLET;
    private RequestQueue mQueue;
    private Activity activity;
    private DBHelper dbHelper;
    private String my_fb_id;
    private int alarmID;
    private boolean imOwnerOfAlarm;
    private String alarm_server_id;

    public GetAlarmTypeHelper(Activity activity, int alarmID) {
        this.activity = activity;
        dbHelper = new DBHelper(activity);
        my_fb_id = dbHelper.getMyIDFromMeTable();
        this.alarmID = alarmID;
        imOwnerOfAlarm = dbHelper.imOwnerOfAlarm(alarmID, my_fb_id);
        alarm_server_id = dbHelper.getServerAlarmId(alarmID);
    }

    public void getMyAlarmTypeFromServer() {
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext())
                .getRequestQueue();
        JSONObject mJSONObject = new JSONObject();


        try {
            mJSONObject.put("server_alarm_id", alarm_server_id);
            if (imOwnerOfAlarm) {
                mJSONObject.put("ownership", "true");
            } else {
                mJSONObject.put("ownership", "false");
            }

            System.out.println("Sending request to sever: GetMyAlarmTypeFromServer");

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

    }

    @Override
    public void onResponse(Object response) {
        String type ="";
        try {
            type = ((JSONObject) response).getString("type");
            System.out.println("Get my Alarm type from server: my type is " + type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dbHelper.editAlarmType(alarmID, my_fb_id, alarmTypeInt(type));
    }

    private int alarmTypeInt(String type_str) {
        if (type_str.equals("default")) {
            return 0;
        } else if (type_str.equals("quiz")) {
            return 1;
        } else if (type_str.equals("video")) {
            return 2;
        } else if (type_str.equals("shake")){
            return 3;
        }
        return -1;
    }
}
