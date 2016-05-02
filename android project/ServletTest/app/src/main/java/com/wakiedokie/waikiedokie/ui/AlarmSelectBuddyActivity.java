package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.util.database.DBHelper;
import com.wakiedokie.waikiedokie.util.userio.UserTableHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmSelectBuddyActivity extends Activity {
    private static final String TAG = "wakiebooboo";
    private LinearLayout buddyContainer;
    private int alarmID;
    private DBHelper dbHelper;
    private String mBuddy = null;
    private String mBuddyID = null;
    private int numChecked = 0;
    List<CheckBox> checkBoxList = new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buddy);
        dbHelper = new DBHelper(this);

        // alarmID is -1 if not passed in from intent (New alarm)
        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);
        final String calMillis = thisIntent.getStringExtra("calMillis");
        System.out.println("calMillis: " + calMillis);


        buddyContainer = (LinearLayout) findViewById(R.id.buddy_container);

        RelativeLayout.LayoutParams paramsBlockRL = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        Cursor cursor = dbHelper.getAllUsers();
        while (cursor.moveToNext()) {

            RelativeLayout buddyRL = new RelativeLayout(this);
            buddyRL.setLayoutParams(paramsBlockRL);
            buddyRL.setPadding(10, 10, 10, 10);

            String fName = cursor.getString(cursor.getColumnIndex("first_name"));
            String lName = cursor.getString(cursor.getColumnIndex("last_name"));
            final String id = cursor.getString(cursor.getColumnIndex(dbHelper.USER_INFO_COLUMN_FACEBOOK_ID));
            final String buddyName = fName + " " + lName;
            Log.d(TAG, buddyName);

            TextView buddyTV = new TextView(this);
            buddyTV.setText(buddyName);
            buddyTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40f);

            final CheckBox buddyCB = new CheckBox(this);
            checkBoxList.add(buddyCB);
            buddyCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        numChecked++;
                        mBuddy = buddyName;
                        mBuddyID = id;
                        for (CheckBox cb : checkBoxList) {
                            cb.setChecked(false);
                        }
                        buddyCB.setChecked(true);
                    } else {
                        numChecked--;
                    }
                }
            });
            RelativeLayout.LayoutParams paramsCheckBox = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsCheckBox.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            buddyCB.setLayoutParams(paramsCheckBox);

            buddyRL.addView(buddyTV);
            buddyRL.addView(buddyCB);
            buddyContainer.addView(buddyRL);
        }

        Button btn = (Button) findViewById(R.id.btn_choose_buddy_send_request);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmSelectBuddyActivity.this, AlarmEditTimeActivity.class);
                intent.putExtra("alarmID", alarmID);
                intent.putExtra("buddy", mBuddy);
                intent.putExtra("buddyID", mBuddyID);
                if (calMillis != null) {
                    intent.putExtra("calMillis", calMillis);
                }
                startActivity(intent);
            }
        });
    }

}
