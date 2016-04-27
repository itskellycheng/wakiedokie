package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.model.Alarm;
import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.util.CustomJSONObjectRequest;
import com.wakiedokie.waikiedokie.util.CustomVolleyRequestQueue;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmConfirmActivity extends Activity implements Response.Listener,
        Response.ErrorListener {

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = "http://128.237.166.30:8080/AndroidAppServlet/SetAlarmRequestServlet";
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_alarm);

        /* Replace this by getting alarm info from other activities later*/
        final User sender = new User("1151451178206737", "Victor", "Chao");
        final User receiver = new User("8888888888888888", "Kelly", "Cheng");
        final String time = "04/30/2016-01:00";
        final String type = "quiz";

        final Alarm alarm = new Alarm(sender, receiver, time, type);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        Button btn = (Button) findViewById(R.id.btn_confirm_alarm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject mJSONObject = new JSONObject();

                try {
                    mJSONObject.put("user1_facebook_id", sender.getFacebookId());
                    mJSONObject.put("user2_facebook_id", receiver.getFacebookId());
                    mJSONObject.put("time", time);
                    mJSONObject.put("type", type);
                    System.out.println("JSON object ready to be sent to SetAlarmRequestServlet");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                        mJSONObject, AlarmConfirmActivity.this, AlarmConfirmActivity.this);
                jsonRequest.setTag(REQUEST_TAG);
                mQueue.add(jsonRequest);
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("ERROR FROM SERVER");
        Toast.makeText(this, "ERROR: Check server's response", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AlarmConfirmActivity.this, AlarmMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(Object response) {
        System.out.println("SUCCESS");
        Toast.makeText(this, "SUCCESS: server responded in success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AlarmConfirmActivity.this, AlarmMainActivity.class);
        startActivity(intent);
    }
}
