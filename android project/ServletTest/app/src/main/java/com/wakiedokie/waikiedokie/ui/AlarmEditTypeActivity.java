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
public class AlarmEditTypeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm_type);


        Button btn_default = (Button) findViewById(R.id.option1);
        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmEditTypeActivity.this, AlarmMainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_quiz = (Button) findViewById(R.id.option2);
        btn_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmEditTypeActivity.this, SetQuizActivity.class);
                startActivity(intent);
            }
        });

        Button btn_video = (Button) findViewById(R.id.option3);
        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmEditTypeActivity.this, TakeVideoActivity.class);
                startActivity(intent);
            }
        });

        Button btn_shake = (Button) findViewById(R.id.option4);
        btn_shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmEditTypeActivity.this, AlarmMainActivity.class);
                startActivity(intent);
            }
        });




    }
}
