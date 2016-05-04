package com.wakiedokie.waikiedokie.integration.remote;

import android.app.Activity;
import android.media.MediaPlayer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.database.DBHelper;
import com.wakiedokie.waikiedokie.util.CustomJSONObjectRequest;
import com.wakiedokie.waikiedokie.util.CustomVolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaovictorshin-deh on 5/3/16.
 */
public class EditAlarmTypeHelper implements Response.Listener,
        Response.ErrorListener {

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = Connection.EDIT_ALARM_TYPE_SERVLET;
    private RequestQueue mQueue;
    private Activity activity;
    private DBHelper dbHelper;
    private String my_fb_id;
    private int alarmID;
    private boolean imOwnerOfAlarm;
    private int type;
    private String alarm_server_id;
    public EditAlarmTypeHelper(Activity activity, int alarmID, int type) {
        this.activity = activity;
        dbHelper = new DBHelper(activity);
        my_fb_id = dbHelper.getMyIDFromMeTable();
        this.alarmID = alarmID;
        imOwnerOfAlarm = dbHelper.imOwnerOfAlarm(alarmID, my_fb_id);
        alarm_server_id = dbHelper.getServerAlarmId(alarmID);
        this.type= type;
    }


    public void sendEditAlarmTypeToServer() {
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext())
                .getRequestQueue();
        JSONObject mJSONObject = new JSONObject();


        try {
            mJSONObject.put("server_alarm_id", alarm_server_id);
            mJSONObject.put("type", alarmTypeString(type));
            if (imOwnerOfAlarm) {
                mJSONObject.put("ownership", "true");
            } else {
                mJSONObject.put("ownership", "false");
            }

            System.out.println("Sending request to sever: sendEditAlarmTypeToServer");

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
        System.out.println("Error response in EditAlarmServlet");
    }

    @Override
    public void onResponse(Object response) {
        System.out.println("SUCCESS: responded from EditAlarmServlet");

    }

    public String alarmTypeString(int type_int) {
        if (type_int == 0) {
            return "default";
        } else if (type_int == 1) {
            return "quiz";
        } else if (type_int == 2) {
            return "video";
        } else if (type_int == 3){
            return "shake";
        }
        return "alarm type: None above";
    }



}
