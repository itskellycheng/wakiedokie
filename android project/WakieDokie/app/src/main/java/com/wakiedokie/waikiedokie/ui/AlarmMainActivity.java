package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.TypedValue;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.database.DBHelper;
import com.wakiedokie.waikiedokie.integration.remote.MyTimerTask;
import com.wakiedokie.waikiedokie.integration.remote.UserTableHelper;

import java.util.Timer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Main page of app
 *
 * Created by chaovictorshin-deh on 4/13/16.
 */

public class AlarmMainActivity extends AppCompatActivity {
    private static boolean RUN_ONCE = true;
    private static int TASK_PERIOD = 10000;
    private User user;
    private int numShowingAlarms;

    // init ui
    private DBHelper dbHelper;
    private LinearLayout timeContainer;
    private final int PENDING_CODE_OFFSET = 990000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);

        setContentView(R.layout.activity_alarm_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("WakieDokie");

        Cursor result = dbHelper.getMe();
        result.moveToFirst();
        String facebook_id = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_FACEBOOK_ID));
        String first_name = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_FIRST_NAME));
        String last_name = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_LAST_NAME));
        user = new User(facebook_id, first_name, last_name);
        UserTableHelper utHelper = new UserTableHelper(this);


        // call this only for the first time this activity is opened
        startTimerTask(user, this);
        utHelper.sendRequestToServer();

        ImageButton imgBtn = (ImageButton) findViewById(R.id.btn_main_add_alarm);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmMainActivity.this, AlarmEditTimeActivity.class);
                startActivity(intent);
            }
        });

        // insert fake users for debuggin
        Button fakeBtn = (Button)findViewById(R.id.btn_populate_fake_user);
        fakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.insertInfo("12345CB", "Charlie", "Brown");
                dbHelper.insertInfo("54321PP", "Peppermint", "Patty");
                dbHelper.insertInfo("22333SB", "Sally", "Brown");
                Toast.makeText(getApplicationContext(), "Saved fake users", Toast.LENGTH_SHORT).show();
            }
        });

//        populateTimeContainer();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        timeContainer = (LinearLayout) findViewById(R.id.time_container);
        timeContainer.removeAllViewsInLayout();

        populateTimeContainer();

    }

    void populateTimeContainer() {
        dbHelper = new DBHelper(AlarmMainActivity.this);
        int numberOfRowsAlarmTable = dbHelper.numberOfRowsAlarmTable();
        Cursor cursor = dbHelper.getAllAlarms();

        int i = 1;
        while (cursor.moveToNext()) {
            String alarmTime = cursor.getString(cursor.getColumnIndex("alarm_time"));
            final int alarmID = cursor.getInt(cursor.getColumnIndex("id"));
            final int alarmStatus = cursor.getInt(cursor.getColumnIndex("is_active"));
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(alarmTime));
            final Calendar finalCal = cal;
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            String amPm;
            if (cal.get(Calendar.AM_PM) == 0)
                amPm = "AM";
            else
                amPm = "PM";
            String timeStr = String.format("%02d:%02d %s", hour, minute, amPm );

            TextView timeTV = new TextView(this);
            timeTV.setText(timeStr);
            timeTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40f);
            timeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    if (alarmStatus == dbHelper.ALARM_PENDING) {
                        Toast.makeText(getApplicationContext(),
                                "Please wait for buddy to approve alarm",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (alarmStatus == dbHelper.ALARM_TYPE_NOT_SET) {
                        intent = new Intent(AlarmMainActivity.this, AlarmEditTypeActivity.class);
                    }
                    else {
                        intent = new Intent(AlarmMainActivity.this, AlarmEditTimeActivity.class);
                    }

                    if (intent != null) {
                        intent.putExtra("alarmID", alarmID);
                        intent.putExtra("alarmStatus", alarmStatus);
                        startActivity(intent);
                    }
                }
            });


            RelativeLayout alarmRL = new RelativeLayout(this);
            LinearLayout.LayoutParams paramsAlarmRL = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            alarmRL.setLayoutParams(paramsAlarmRL);
            alarmRL.setPadding(10, 10, 10, 10);

            /* local alarm */
            if (alarmStatus == dbHelper.ALARM_LOCAL_ACTIVE ||
                    alarmStatus == dbHelper.ALARM_LOCAL_INACTIVE) {

                Switch mSwitch = new Switch(AlarmMainActivity.this);
                RelativeLayout.LayoutParams paramsSwitch = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsSwitch.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mSwitch.setLayoutParams(paramsSwitch);
                if (alarmIsActive(alarmID)) {
                    mSwitch.setChecked(true);
                } else {
                    mSwitch.setChecked(false);
                }
                mSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleAlarm(alarmID, finalCal);
                    }
                });

                alarmRL.addView(mSwitch);
            }

            /* alarm with alarm buddy */
            else {
                // for debugging
                timeTV.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Intent intent = new Intent(AlarmMainActivity.this, AlarmEditTypeActivity.class);
                        intent.putExtra("alarmID", alarmID);
                        intent.putExtra("alarmStatus", alarmStatus);
                        startActivity(intent);
                        return true;
                    }
                });

                String buddyName = dbHelper.getBuddyName(alarmID);
                TextView buddyTV = new TextView(this);
                RelativeLayout.LayoutParams paramsBuddy = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsBuddy.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                buddyTV.setLayoutParams(paramsBuddy);
                buddyTV.setText(buddyName);
                buddyTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f);
                alarmRL.addView(buddyTV);
            }

            alarmRL.addView(timeTV);
            timeContainer.addView(alarmRL);
        }
    }
    /* Helper function for toggling alarm on/off */
    private void toggleAlarm(int id, Calendar cal) {
        AlarmManager am =
                (AlarmManager)getSystemService(Activity.ALARM_SERVICE);

        Cursor c = dbHelper.getAlarm(id);
        c.moveToFirst();
        int isActive = c.getInt(c.getColumnIndex("is_active"));

        if (isActive == 0) {
            dbHelper.setAlarmToActive(id);

            cal = getCorrectDate(cal, id);

            int requestCode = PENDING_CODE_OFFSET + id;

            Intent alarmRingIntent = new Intent(AlarmMainActivity.this, AlarmStatusActivity.class);
            alarmRingIntent.putExtra("alarmID", id);
            PendingIntent pendingIntent = PendingIntent.getActivity(AlarmMainActivity.this,
                    requestCode, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    pendingIntent);
        }
        else {
            dbHelper.setAlarmToInactive(id);

            int requestCode = PENDING_CODE_OFFSET + id;

            Intent alarmRingIntent = new Intent(AlarmMainActivity.this, AlarmStatusActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(AlarmMainActivity.this,
                    requestCode, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(AlarmMainActivity.this,
                    12345, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            pendingIntent.cancel();
            am.cancel(pendingIntent);
            pendingIntent2.cancel();
            am.cancel(pendingIntent2);
        }
    }


    /* Helper function to check in database if alarm is active or not */
    private boolean alarmIsActive(int id) {
        Cursor c = dbHelper.getAlarm(id);
        c.moveToFirst();
        int isActive = c.getInt(c.getColumnIndex("is_active"));
        if (isActive == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /* Helper function to check if the alarm needs to be set for tomorrow instead of today */
    private Calendar getCorrectDate(Calendar cal, int alarmID) {
        Calendar now = Calendar.getInstance();
        // Set alarm for next day if time is before current time
        if (cal.before(now)) {
            cal.add(Calendar.DATE, 1);
        }
        dbHelper.updateAlarm(alarmID, Long.toString(cal.getTimeInMillis()), "", 1);
        return cal;
    }


    private void startTimerTask(User user, Activity activity) {
        if (RUN_ONCE == true) {
            RUN_ONCE = false;

            MyTimerTask mTimerTask = new MyTimerTask(user, this);
            Timer timer = new Timer();
            // scheduling the task at fixed rate delay
            timer.scheduleAtFixedRate(mTimerTask, 500, TASK_PERIOD);
        }

    }


}
