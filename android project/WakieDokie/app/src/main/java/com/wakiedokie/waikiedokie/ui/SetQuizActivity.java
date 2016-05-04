package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.database.DBHelper;
import com.wakiedokie.waikiedokie.integration.remote.EditAlarmTypeHelper;

import org.w3c.dom.Text;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class SetQuizActivity extends Activity {
    String preAnswerText = "My answer is: ";
    boolean answerSelected;
    private DBHelper dbHelper;
    int alarmID;
    private EditAlarmTypeHelper eatHelper;
    private Activity activity;
    private String my_fb_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_quiz);
        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);
        dbHelper = new DBHelper(this);
        activity = this;
        my_fb_id = dbHelper.getMyIDFromMeTable();

        answerSelected = false;

        final TextView answerDisplayTV = (TextView)findViewById(R.id.answer_display);

        final Button btn_answer_1 = (Button) findViewById(R.id.btn_answer_1);
        btn_answer_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerDisplayTV.setText(preAnswerText + btn_answer_1.getText());
                answerSelected = true;
            }
        });

        final Button btn_answer_2 = (Button) findViewById(R.id.btn_answer_2);
        btn_answer_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerDisplayTV.setText(preAnswerText + btn_answer_2.getText());
                answerSelected = true;
            }
        });

        final Button btn_answer_3 = (Button) findViewById(R.id.btn_answer_3);
        btn_answer_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerDisplayTV.setText(preAnswerText + btn_answer_3.getText());
                answerSelected = true;
            }
        });

        Button btn = (Button) findViewById(R.id.btn_confirm_quiz);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answerSelected) {
                    eatHelper = new EditAlarmTypeHelper(activity, alarmID, DBHelper.ALARM_TYPE_QUIZ);
                    eatHelper.sendEditAlarmTypeToServer();
                    dbHelper.editAlarmType(alarmID, my_fb_id, DBHelper.ALARM_TYPE_QUIZ);
                    dbHelper.printAllAlarms();
                    Intent intent = new Intent(SetQuizActivity.this, AlarmMainActivity.class);
                    startActivity(intent);
                }
                else {
                    String toastStr = "Please set an answer!";
                    Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
