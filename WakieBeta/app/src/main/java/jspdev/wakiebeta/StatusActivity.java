package jspdev.wakiebeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView boy_name_tv = (TextView) findViewById(R.id.boy_name);
        TextView girl_name_tv = (TextView) findViewById(R.id.girl_name);
        TextView girl_status_tv = (TextView) findViewById(R.id.girl_status);
        TextView boy_status_tv = (TextView) findViewById(R.id.boy_status);
        ImageView boy_img_iv = (ImageView) findViewById(R.id.boy_img);
        ImageView girl_img_iv = (ImageView) findViewById(R.id.girl_img);


    }

    /* Called when the user clicks the turn off alarm button */
    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
    }

}
