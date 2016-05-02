package com.wakiedokie.waikiedokie.util.userio;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wakiedokie.waikiedokie.dialogs.Message;
import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.ui.AlarmEditTimeActivity;
import com.wakiedokie.waikiedokie.ui.AlarmSelectBuddyActivity;
import com.wakiedokie.waikiedokie.util.CustomJSONObjectRequest;
import com.wakiedokie.waikiedokie.util.CustomVolleyRequestQueue;
import com.wakiedokie.waikiedokie.util.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaovictorshin-deh on 5/1/16.
 */
public class UserTableHelper implements Response.Listener,
        Response.ErrorListener {
    public static final String REQUEST_TAG = "MainVolleyActivity";
    private static final String SERVER_URL = "http://10.0.0.25:8080/AndroidAppServlet/UserTableIOServlet";
    private RequestQueue mQueue;
    private Activity activity;
    private DBHelper dbHelper;
    private String my_fb_id;


    public UserTableHelper(Activity activity) {
        this.activity = activity;
        dbHelper = new DBHelper(activity);
        my_fb_id = dbHelper.getMyIDFromMeTable();
    }


    public void sendRequestToServer() {
        mQueue = CustomVolleyRequestQueue.getInstance(activity.getApplicationContext())
                .getRequestQueue();
        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject.put("request_for_user_table", my_fb_id);
            System.out.println("Sending request to sever: client needs user_table info");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, SERVER_URL,
                mJSONObject, this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Error response from UserTableIOServlet");
    }

    @Override
    public void onResponse(Object response) {

        // get array list json object of users and populate local user table
    try {
        // 1. check the status of alarm I set before
        String users_js_str = ((JSONObject) response).getString("user_table_server");
        System.out.println("Trying to parse response user_table_server");
        JSONArray jsonarray = new JSONArray(users_js_str);
        System.out.println(jsonarray.toString());
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String facebook_id = jsonobject.getString("facebook_id");
            String first_name = jsonobject.getString("first_name");
            String last_name = jsonobject.getString("last_name");
            System.out.println("Facebook id: " + facebook_id + ", First name: " + first_name
                    + ", Last_name: " + last_name);
            dbHelper.insertInfo(facebook_id, first_name, last_name);
        }

    } catch (JSONException e) {
        e.printStackTrace();
    }



    }

}
