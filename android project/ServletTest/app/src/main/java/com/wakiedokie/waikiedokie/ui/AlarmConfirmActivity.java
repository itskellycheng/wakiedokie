package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.wakiedokie.waikiedokie.util.database.DBHelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmConfirmActivity extends Activity implements Response.Listener,
        Response.ErrorListener {

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = "http://128.237.133.8:8080/AndroidAppServlet/SetAlarmRequestServlet";
    private RequestQueue mQueue;

    private static final int PENDING_CODE_OFFSET = 990000;
    private static final String TAG = "AlarmConfirmActivity";
    private int alarmID;
    private String mBuddy;
    private String mBuddyID;
    private String timeStr;
    private Calendar cal = Calendar.getInstance();
    private DBHelper dbHelper;
    private AlarmManager am;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_alarm);
        dbHelper = new DBHelper(this);
        am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);

        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);
        mBuddy = thisIntent.getStringExtra("buddy");
        mBuddyID = thisIntent.getStringExtra("buddyID");
        timeStr = thisIntent.getStringExtra("timeStr");
        String calStr = thisIntent.getStringExtra("calMillis");
        cal.setTimeInMillis(Long.parseLong(calStr));


        /* Replace this by getting alarm info from other activities later*/
        final User sender = new User("1151451178206737", "Victor", "Chao");
        final User receiver = new User("10209500772462847", "Kelly", "Cheng");
        final String time = "04/30/2016-01:00";
        final String type = "quiz";
        dbHelper = new DBHelper(this);
        final Alarm alarm = new Alarm(sender, receiver, time, type);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        /****************
         * Send Request button
         ****************/
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

                // Database update/insert
                dbHelper.addOrUpdateAlarm(alarmID, Long.toString(cal.getTimeInMillis()),
                        dbHelper.getMyIDFromMeTable(), mBuddyID, dbHelper.ALARM_TYPE_NOT_SET);

                int requestCode = PENDING_CODE_OFFSET + alarmID;
                Intent alarmRingIntent = new Intent(AlarmConfirmActivity.this, AlarmStatusActivity.class);
                alarmRingIntent.putExtra("alarmID", alarmID);
                PendingIntent pendingIntent = PendingIntent.getActivity(AlarmConfirmActivity.this,
                        requestCode, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        pendingIntent);

                makeCountdownToast(cal);

                Intent mainIntent = new Intent(AlarmConfirmActivity.this, AlarmMainActivity.class);
                startActivity(mainIntent);

            }
        });

        TextView buddyTV = (TextView)findViewById(R.id.textBuddy);
        buddyTV.setText(mBuddy);

        TextView clockTV = (TextView)findViewById(R.id.textClock);
        clockTV.setText(timeStr);

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

    /* Helper function to make toast that shows how much time till alarm goes off */
    private void makeCountdownToast(Calendar cal) {

        // Logging stuff
        Log.d(TAG, "Alarm is set");
        Calendar now = Calendar.getInstance();
        Long hours = TimeUnit.MILLISECONDS.toHours(
                cal.getTimeInMillis()-now.getTimeInMillis());
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(
                cal.getTimeInMillis() - now.getTimeInMillis() - TimeUnit.HOURS.toMillis(hours));
        String hoursStr = Long.toString(hours);
        String minutesStr = Long.toString(minutes);
        String toastStr = "Alarm will go off in " + hoursStr + " hrs " + minutesStr + " mins";
        Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();

    }
}
