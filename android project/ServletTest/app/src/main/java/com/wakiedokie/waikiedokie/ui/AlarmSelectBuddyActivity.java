package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wakiedokie.waikiedokie.R;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmSelectBuddyActivity extends Activity {
    private int alarmID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buddy);

        // alarmID is -1 if not passed in from intent (New alarm)
        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);closeContextMenu();

        Button btn = (Button) findViewById(R.id.btn_choose_buddy_send_request);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmSelectBuddyActivity.this, AlarmEditTimeActivity.class);
                intent.putExtra("alarmID", alarmID);
                startActivity(intent);
            }
        });
    }
}
