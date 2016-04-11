package jspdev.wakiebeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView boy_message_tv = (TextView) findViewById(R.id.boy_message);
        TextView boy_name_tv = (TextView) findViewById(R.id.boy_name);

    }

    public void confirmRequest(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);
        // stay on this page
    }
    public void declineRequest(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);
        // stay on this page
    }
}
