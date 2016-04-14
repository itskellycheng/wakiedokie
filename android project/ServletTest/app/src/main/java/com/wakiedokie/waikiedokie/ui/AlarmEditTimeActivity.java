package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.wakiedokie.waikiedokie.R;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmEditTimeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);



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
                Intent intent = new Intent(AlarmEditTimeActivity.this, AlarmConfirmActivity.class);
                startActivity(intent);
            }
        });
    }
}
