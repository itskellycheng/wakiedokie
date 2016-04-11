package jspdev.wakiebeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class EditAlarmTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void goDefault(View view) {
        Intent intent = new Intent(this, MainActivity.class);
    }

    public void goQuiz(View view) {
        Intent intent = new Intent(this, MainActivity.class);
    }
    public void goVideo(View view) {
        Intent intent = new Intent(this, TakeVideoActivity.class);
    }
    public void goShake(View view) {
        Intent intent = new Intent(this, MainActivity.class);
    }
}
