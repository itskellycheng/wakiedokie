package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        timeContainer = (LinearLayout) findViewById(R.id.time_container);

        dbHelper = new DBHelper(AlarmMainActivity.this);
        Cursor cursor = dbHelper.getAllAlarms();
//        String alarmTime = cursor.getString(cursor.getColumnIndex("alarm_time"));
//        System.out.println("Alarm time = " + alarmTime);
        while (cursor.moveToNext()) {
            String alarmTime = cursor.getString(cursor.getColumnIndex("alarm_time"));
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(alarmTime));
            String hour = Integer.toString(cal.get(Calendar.HOUR));
            String minute = Integer.toString(cal.get(Calendar.MINUTE));
            String amPm;
            if (cal.get(Calendar.AM_PM) == 0)
                amPm = "AM";
            else
                amPm = "PM";
            String timeStr = hour + ":" + minute + " " + amPm;

            TextView alarmTV = new TextView(this);
            alarmTV.setText(timeStr);
            alarmTV.setLayoutParams(params);

            timeContainer.addView(alarmTV);
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
}
