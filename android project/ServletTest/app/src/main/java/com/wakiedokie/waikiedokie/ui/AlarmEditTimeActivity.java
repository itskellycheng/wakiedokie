package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.util.database.DBHelper;

import java.util.Calendar;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmEditTimeActivity extends Activity {
    private static final String TAG = "AlarmEditActivity";
    private TimePicker alarmTimePicker;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm_time);
        dbHelper = new DBHelper(AlarmEditTimeActivity.this);

        Intent thisIntent = getIntent();
        final int alarmID = thisIntent.getIntExtra("alarmID", 1);closeContextMenu();

        Cursor cursor = dbHelper.getAlarm(alarmID);
        cursor.moveToFirst();
        String alarmTime = cursor.getString(cursor.getColumnIndex("alarm_time"));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(alarmTime));

        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmTimePicker.setHour(cal.get(Calendar.HOUR_OF_DAY));
            alarmTimePicker.setMinute(cal.get(Calendar.MINUTE));
        }
        else {
            alarmTimePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            alarmTimePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        }

        Button btn_select_buddy = (Button) findViewById(R.id.btn_to_select_buddy);
        btn_select_buddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmEditTimeActivity.this, AlarmSelectBuddyActivity.class);
                startActivity(intent);
            }
        });

        Button btn_save = (Button) findViewById(R.id.btn_save_alarm);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cal.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                    cal.set(Calendar.MINUTE, alarmTimePicker.getMinute());
                }
                else {
                    cal.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                    cal.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                }

                Intent alarmRingIntent = new Intent(AlarmEditTimeActivity.this, AlarmStatusActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(AlarmEditTimeActivity.this,
                        12345, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am =
                        (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        pendingIntent);
                Log.d(TAG, "Alarm is set");

                // Save alarm to SQlite
                dbHelper.addAlarm(Long.toString(cal.getTimeInMillis()), "", 1);
                Cursor cursor = dbHelper.getAllAlarms();
                cursor.moveToFirst();
                String alarmTime = cursor.getString(cursor.getColumnIndex("alarm_time"));
                System.out.println("Alarm time = " + alarmTime);
                while (cursor.moveToNext()) {
                    alarmTime = cursor.getString(cursor.getColumnIndex("alarm_time"));
                    Log.d(TAG, "Alarm time = " + alarmTime);
                    Calendar calCheck = Calendar.getInstance();
                    calCheck.setTimeInMillis(Long.parseLong(alarmTime));
                    Log.d(TAG, "Hour  : " + calCheck.get(Calendar.HOUR));
                    Log.d(TAG, "Minute : " + calCheck.get(Calendar.MINUTE));
                    Log.d(TAG, "AM PM : " + calCheck.get(Calendar.AM_PM));
                }

                dbHelper.setAlarmToActive(alarmID);

                Intent mainIntent = new Intent(AlarmEditTimeActivity.this, AlarmMainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
