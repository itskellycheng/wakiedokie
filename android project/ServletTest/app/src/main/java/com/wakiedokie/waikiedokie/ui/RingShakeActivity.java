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
public class RingShakeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_shake);


        Button btn = (Button) findViewById(R.id.btn_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingShakeActivity.this, AlarmStatusActivity.class);
                startActivity(intent);
            }
        });
    }

}
