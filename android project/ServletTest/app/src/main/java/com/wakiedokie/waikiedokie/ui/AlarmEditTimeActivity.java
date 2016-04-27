package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.wakiedokie.waikiedokie.R;

import java.util.Calendar;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmEditTimeActivity extends Activity {
    private static final String TAG = "AlarmEditActivity";
    private TimePicker alarmTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm_time);

        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);

        // Create an offset from the current time in which the alarm will go off


//        cal.add(Calendar.SECOND, 5);



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

                Intent mainIntent = new Intent(AlarmEditTimeActivity.this, AlarmMainActivity.class);
                Intent alarmRingIntent = new Intent(AlarmEditTimeActivity.this, AlarmStatusActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(AlarmEditTimeActivity.this,
                        12345, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am =
                        (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        pendingIntent);
                Log.d(TAG, "Alarm is set");

                startActivity(mainIntent);
            }
        });
    }
}
