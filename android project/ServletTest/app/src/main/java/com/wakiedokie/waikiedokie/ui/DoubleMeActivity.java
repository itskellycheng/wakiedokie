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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.util.CustomJSONObjectRequest;
import com.wakiedokie.waikiedokie.util.CustomVolleyRequestQueue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaovictorshin-deh on 4/12/16.
 */

public class DoubleMeActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener,
        Response.ErrorListener {
    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = "http://128.237.221.10:8080/AndroidAppServlet/UserServlet";
    private RequestQueue mQueue;

    EditText inputValue = null;
    Integer doubledValue = 0;
    Button doubleMe;
    String current_user_facebook_id;
    String current_user_first_name;
    String current_user_last_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_me);

        inputValue = (EditText) findViewById(R.id.inputNum);
        doubleMe = (Button) findViewById(R.id.doubleme);

        doubleMe.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            current_user_facebook_id = extras.getString("current_user_facebook_id");
            current_user_first_name = extras.getString("current_user_first_name");
            current_user_last_name = extras.getString("current_user_last_name");
            System.out.println("extra is not null");
            System.out.println(extras.toString());
            System.out.println(current_user_facebook_id);
            System.out.println(current_user_first_name);
            System.out.println(current_user_last_name);
        } else {
            System.out.println("Extra is null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        inputValue.setText(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
        inputValue.setText("Response is: " + response);
        try {
            inputValue.setText(inputValue.getText() + "\n\n" + ((JSONObject) response).getString
                    ("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                JSONObject mJSONObject = new JSONObject();

                try {
                    mJSONObject.put("facebook_id", current_user_facebook_id);
                    mJSONObject.put("first_name", current_user_first_name);
                    mJSONObject.put("last_name", current_user_last_name);
                    System.out.println(current_user_facebook_id);
                    System.out.println("JSON object ready to be sent");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                        mJSONObject, this, this);
                jsonRequest.setTag(REQUEST_TAG);
                mQueue.add(jsonRequest);
        }
    }
}