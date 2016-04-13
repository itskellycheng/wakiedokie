package kellycheng.servlettest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import kellycheng.servlettest.util.CustomJSONObjectRequest;
import kellycheng.servlettest.util.CustomVolleyRequestQueue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener,
        Response.ErrorListener{

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private RequestQueue mQueue;
    String url = "http://10.0.2.2:5005/ServletTest/DoubleMeServlet";

    EditText inputValue=null;
    Integer doubledValue =0;
    Button doubleMeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        inputValue = (EditText) findViewById(R.id.inputNum);
        doubleMeBtn = (Button) findViewById(R.id.doubleme);

        doubleMeBtn.setOnClickListener(this);
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
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
//        String url = "http://10.0.2.2:5005/ServletTest/DoubleMeServlet";
//        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url,
//                new JSONObject(), this, this);
//        jsonRequest.setTag(REQUEST_TAG);
//
//        doubleMeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mQueue.add(jsonRequest);
//            }
//        });
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
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.doubleme:

                JSONObject mJSONObject = new JSONObject();

                try {
                    mJSONObject.put("username", "my username");
                    mJSONObject.put("password", "my password");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url,
                        mJSONObject, this, this);
                jsonRequest.setTag(REQUEST_TAG);
                mQueue.add(jsonRequest);




//                new Thread(new Runnable() {
//                    public void run() {
//
//                        try{
//                            URL url = new URL("http://10.0.2.2:5005/ServletTest/DoubleMeServlet");
//                            URLConnection connection = url.openConnection();
//
//                            String inputString = inputValue.getText().toString();
//                            //inputString = URLEncoder.encode(inputString, "UTF-8");
//
//                            Log.d("inputString", inputString);
//
//                            connection.setDoOutput(true);
//                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//                            out.write(inputString);
//                            out.close();
//
//                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                            String returnString="";
//                            doubledValue =0;
//
//                            while ((returnString = in.readLine()) != null)
//                            {
//                                doubledValue= Integer.parseInt(returnString);
//                            }
//                            in.close();
//
//
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//
//                                    inputValue.setText(doubledValue.toString());
//
//                                }
//                            });
//
//                        }catch(Exception e)
//                        {
//                            Log.d("Exception",e.toString());
//                        }
//
//                    }
//                }).start();

                break;
        }
    }
}
