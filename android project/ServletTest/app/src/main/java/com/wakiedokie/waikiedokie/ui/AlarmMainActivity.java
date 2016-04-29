package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;

import android.util.TypedValue;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.util.database.DBHelper;
import com.wakiedokie.waikiedokie.util.timer.MyTimerTask;
import java.util.Timer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Main page of app
 *
 * Created by chaovictorshin-deh on 4/13/16.
 */
public class AlarmMainActivity extends Activity {

    private User user;

    // init ui
    private DBHelper dbHelper;
    private LinearLayout timeContainer;
    private final int PENDING_CODE_OFFSET = 990000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_alarm_main);

        Cursor result = dbHelper.getData(1);
        result.moveToFirst();
        String facebook_id = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_FACEBOOK_ID));
        String first_name = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_FIRST_NAME));
        String last_name = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_LAST_NAME));
        user = new User(facebook_id, first_name, last_name);

        MyTimerTask mTimerTask = new MyTimerTask(user, this);
        Timer timer = new Timer();
        // scheduling the task at fixed rate delay
        timer.scheduleAtFixedRate(mTimerTask, 500, 15000);

        LinearLayout.LayoutParams paramsAlarmRL = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        timeContainer = (LinearLayout) findViewById(R.id.time_container);

        dbHelper = new DBHelper(AlarmMainActivity.this);
        Cursor cursor = dbHelper.getAllAlarms();
        while (cursor.moveToNext()) {
            String alarmTime = cursor.getString(cursor.getColumnIndex("alarm_time"));
            final int alarmID = cursor.getInt(cursor.getColumnIndex("id"));
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(alarmTime));
            final Calendar finalCal = cal;
            String hour = Integer.toString(cal.get(Calendar.HOUR));
            String minute = Integer.toString(cal.get(Calendar.MINUTE));
            String amPm;
            if (cal.get(Calendar.AM_PM) == 0)
                amPm = "AM";
            else
                amPm = "PM";
            String timeStr = hour + ":" + minute + " " + amPm;

            TextView timeTV = new TextView(this);
            timeTV.setText(timeStr);
            timeTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40f);
            timeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AlarmMainActivity.this, AlarmEditTimeActivity.class);
                        intent.putExtra("alarmID", alarmID);
                        startActivity(intent);
                    }
                }
            );

            RelativeLayout alarmRL = new RelativeLayout(this);
            alarmRL.setLayoutParams(paramsAlarmRL);
            alarmRL.setPadding(10, 10, 10, 10);

            Switch mSwitch = new Switch(AlarmMainActivity.this);
            RelativeLayout.LayoutParams paramsSwitch = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsSwitch.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mSwitch.setLayoutParams(paramsSwitch);
            if (alarmIsActive(alarmID)) {
                mSwitch.setChecked(true);
            }
            else {
                mSwitch.setChecked(false);
            }
            mSwitch.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                    toggleAlarm(alarmID, finalCal);
                 }
            });

            alarmRL.addView(timeTV);
            alarmRL.addView(mSwitch);
            timeContainer.addView(alarmRL);
        }

        ImageButton imgBtn = (ImageButton) findViewById(R.id.btn_main_add_alarm);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmMainActivity.this, AlarmEditTimeActivity.class);
                startActivity(intent);
            }
        });

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
}
