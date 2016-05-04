package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.integration.remote.WakeUpHelper;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class RingQuizActivity extends Activity {
    int quizAnswer;
    final static String TAG = "RingQuizActivity";
    private int alarmID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_quiz);
        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);
        System.out.println("inside RingQuizActivity. Alarm ID:" + alarmID);
        quizAnswer = 0;

        Button btn_answer_1 = (Button) findViewById(R.id.btn_answer_1);
        btn_answer_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Selected ans 1");
                if (quizAnswer == 0)
                    rightAnswer();
                else
                    wrongAnswer();
            }
        });

        Button btn_answer_2 = (Button) findViewById(R.id.btn_answer_2);
        btn_answer_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Selected ans 2");
                if (quizAnswer == 1)
                    rightAnswer();
                else
                    wrongAnswer();
            }
        });

        Button btn_answer_3 = (Button) findViewById(R.id.btn_answer_3);
        btn_answer_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Selected ans 3");
                if (quizAnswer == 2)
                    rightAnswer();
                else
                    wrongAnswer();
            }
        });
    }

    public void wrongAnswer() {
        String toastStr = "Wrong answer! Try again.";
        Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();
    }

    public void rightAnswer() {
        String toastStr = "Yay! Correct answer.";
        Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RingQuizActivity.this, AlarmStatusActivity.class);
        intent.putExtra("alarmID", alarmID);
        startActivity(intent);
    }
}
