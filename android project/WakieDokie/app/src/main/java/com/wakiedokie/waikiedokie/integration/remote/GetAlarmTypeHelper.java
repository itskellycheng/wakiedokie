package com.wakiedokie.waikiedokie.integration.remote;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.database.DBHelper;
import com.wakiedokie.waikiedokie.ui.AlarmMainActivity;
import com.wakiedokie.waikiedokie.ui.AlarmStatusActivity;
import com.wakiedokie.waikiedokie.ui.RingQuizActivity;
import com.wakiedokie.waikiedokie.ui.RingShakeActivity;
import com.wakiedokie.waikiedokie.ui.RingVideoActivity;
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
    Button btn_do_task;
    Button btn_turn_off;
    private WakeUpHelper wuHelper;


    public GetAlarmTypeHelper(Activity activity, int alarmID, Button btn_do_task, Button btn_turn_off, WakeUpHelper wuHelper) {
        this.activity = activity;
        dbHelper = new DBHelper(activity);
        my_fb_id = dbHelper.getMyIDFromMeTable();
        this.alarmID = alarmID;
        imOwnerOfAlarm = dbHelper.imOwnerOfAlarm(alarmID, my_fb_id);
        alarm_server_id = dbHelper.getServerAlarmId(alarmID);
        this.btn_do_task = btn_do_task;
        this.btn_turn_off = btn_turn_off;
        this.wuHelper = wuHelper;
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
        String type_str ="";
        try {
            type_str = ((JSONObject) response).getString("type");
            System.out.println("Get my Alarm type from server: my type is " + type_str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final int type = alarmTypeInt(type_str);


        if (type == DBHelper.ALARM_TYPE_DEFAULT) {
            btn_do_task.setVisibility(View.INVISIBLE);
        }
        btn_do_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == DBHelper.ALARM_TYPE_QUIZ){
                    Intent intent = new Intent(activity, RingQuizActivity.class);
                    intent.putExtra("alarmID", alarmID);
                    activity.startActivity(intent);
                } else if (type == DBHelper.ALARM_TYPE_VIDEO) {
                    Intent intent = new Intent(activity, RingVideoActivity.class);
                    intent.putExtra("alarmID", alarmID);
                    activity.startActivity(intent);
                } else if (type == DBHelper.ALARM_TYPE_SHAKE) {
                    Intent intent = new Intent(activity, RingShakeActivity.class);
                    intent.putExtra("alarmID", alarmID);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Error in type", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (type != DBHelper.ALARM_TYPE_DEFAULT) {
            btn_turn_off.setVisibility(View.INVISIBLE);
        }
        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_turn_off.setClickable(false);

                // instead of stopping the alarm, send message to server and check response: 1. one is awake 2. both awake
                wuHelper.sendWakeUpMessageToServer();


            }
        });

    }

    private int alarmTypeInt(String type_str) {
        if (type_str.equals("default")) {
            return DBHelper.ALARM_TYPE_DEFAULT;
        } else if (type_str.equals("quiz")) {
            return DBHelper.ALARM_TYPE_QUIZ;
        } else if (type_str.equals("video")) {
            return DBHelper.ALARM_TYPE_VIDEO;
        } else if (type_str.equals("shake")){
            return DBHelper.ALARM_TYPE_SHAKE;
        }
        return -1;
    }
}
