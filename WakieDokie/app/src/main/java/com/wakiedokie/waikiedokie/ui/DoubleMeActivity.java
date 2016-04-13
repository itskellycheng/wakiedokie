package com.wakiedokie.waikiedokie.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wakiedokie.waikiedokie.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by chaovictorshin-deh on 4/12/16.
 */

public class DoubleMeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputValue = null;
    Integer doubledValue = 0;
    Button doubleMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_me);

        inputValue = (EditText) findViewById(R.id.inputNum);
        doubleMe = (Button) findViewById(R.id.doubleme);

        doubleMe.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.doubleme:

                new Thread(new Runnable() {
                    public void run() {

                        try {
                            URL url = new URL("http://10.0.0.25:8080/AndroidAppServlet/DoubleMeServlet");
                            URLConnection connection = url.openConnection();

                            String inputString = inputValue.getText().toString();
                            //inputString = URLEncoder.encode(inputString, "UTF-8");

                            Log.d("inputString", inputString);

                            connection.setDoOutput(true);
                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                            out.write(inputString);
                            out.close();

                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                            String returnString = "";
                            doubledValue = 0;

                            while ((returnString = in.readLine()) != null) {
                                doubledValue = Integer.parseInt(returnString);
                            }
                            in.close();


                            runOnUiThread(new Runnable() {
                                public void run() {

                                    inputValue.setText(doubledValue.toString());

                                }
                            });

                        } catch (Exception e) {
                            Log.d("Exception", e.toString());
                        }

                    }
                }).start();

                break;
        }
    }
}