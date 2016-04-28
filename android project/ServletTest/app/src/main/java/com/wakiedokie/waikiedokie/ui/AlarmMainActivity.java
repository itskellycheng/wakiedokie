package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.util.database.DBHelper;

import java.util.Calendar;

/**
 * Main page of app
 *
 * Created by chaovictorshin-deh on 4/13/16.
 */
public class AlarmMainActivity extends Activity {
    private DBHelper dbHelper;
    private LinearLayout timeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);

        LinearLayout.LayoutParams paramsAlarmRL = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        timeContainer = (LinearLayout) findViewById(R.id.time_container);

        dbHelper = new DBHelper(AlarmMainActivity.this);
        Cursor cursor = dbHelper.getAllAlarms();
//        String alarmTime = cursor.getString(cursor.getColumnIndex("alarm_time"));
//        System.out.println("Alarm time = " + alarmTime);
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

        Button btn_dummy = (Button) findViewById(R.id.btn_dummy_to_type);
        btn_dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmMainActivity.this, AlarmEditTypeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void toggleAlarm(int id, Calendar cal) {
        AlarmManager am =
                (AlarmManager)getSystemService(Activity.ALARM_SERVICE);

        Cursor c = dbHelper.getAlarm(id);
        c.moveToFirst();
        int isActive = c.getInt(c.getColumnIndex("is_active"));

        if (isActive == 0) {
            dbHelper.setAlarmToActive(id);

            Intent alarmRingIntent = new Intent(AlarmMainActivity.this, AlarmStatusActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(AlarmMainActivity.this,
                    id, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    pendingIntent);
        }
        else {
            dbHelper.setAlarmToInactive(id);

            Intent alarmRingIntent = new Intent(AlarmMainActivity.this, AlarmStatusActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(AlarmMainActivity.this,
                    id, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(AlarmMainActivity.this,
                    12345, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            pendingIntent.cancel();
            am.cancel(pendingIntent);
            pendingIntent2.cancel();
            am.cancel(pendingIntent2);
        }
    }

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
}
