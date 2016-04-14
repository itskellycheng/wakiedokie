package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.wakiedokie.waikiedokie.R;

/**
 * Created by chaovictorshin-deh on 4/13/16.
 */
public class AlarmMainActivity extends Activity {
    // init ui

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);



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
