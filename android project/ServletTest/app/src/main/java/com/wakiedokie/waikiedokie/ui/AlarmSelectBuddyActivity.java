package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.util.database.DBHelper;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmSelectBuddyActivity extends Activity {
    private LinearLayout buddyContainer;
    private int alarmID;
    private DBHelper dbHelper;
    private String mBuddy = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buddy);

        // alarmID is -1 if not passed in from intent (New alarm)
        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);

        final TextView buddy1_TV = (TextView)findViewById(R.id.buddy1);
        TextView buddy2_TV = (TextView)findViewById(R.id.buddy2);

        buddy1_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBuddy = buddy1_TV.getText().toString();

            }
        });

//        buddyContainer = (LinearLayout) findViewById(R.id.buddy_container);
//
//        RelativeLayout.LayoutParams paramsBlockRL = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//        Cursor cursor = dbHelper.getAllUsers();
//        while (cursor.moveToNext()) {
//
//            RelativeLayout buddyRL = new RelativeLayout(this);
//            buddyRL.setLayoutParams(paramsBlockRL);
//            buddyRL.setPadding(10, 10, 10, 10);
//
//            String fName = cursor.getString(cursor.getColumnIndex("first_name"));
//            String lName = cursor.getString(cursor.getColumnIndex("last_name"));
//            String buddyName = fName + lName;
//
//            TextView buddyTV = new TextView(this);
//            buddyTV.setText(buddyName);
//            buddyTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40f);
//        }

        Button btn = (Button) findViewById(R.id.btn_choose_buddy_send_request);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmSelectBuddyActivity.this, AlarmEditTimeActivity.class);
                intent.putExtra("alarmID", alarmID);
                intent.putExtra("buddy", mBuddy);
                startActivity(intent);
            }
        });
    }
}
