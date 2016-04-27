package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.util.database.DBHelper;
import com.wakiedokie.waikiedokie.util.timer.MyTimerTask;
import java.util.Timer;

/**
 * Created by chaovictorshin-deh on 4/13/16.
 */
public class AlarmMainActivity extends Activity {

    private User user;
    String current_user_facebook_id;
    String current_user_first_name;
    String current_user_last_name;
    DBHelper mydb;
    // init ui

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        setContentView(R.layout.activity_alarm_main);

        Cursor result = mydb.getData(1);
        result.moveToFirst();
        String facebook_id = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_FACEBOOK_ID));
        String first_name = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_FIRST_NAME));
        String last_name = result.getString(result.getColumnIndex(DBHelper.USER_INFO_COLUMN_LAST_NAME));
        user = new User(facebook_id, first_name, last_name);

        MyTimerTask mTimerTask = new MyTimerTask(user, this);
        Timer timer = new Timer();
        // scheduling the task at fixed rate delay
        timer.scheduleAtFixedRate(mTimerTask, 500, 15000);

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
