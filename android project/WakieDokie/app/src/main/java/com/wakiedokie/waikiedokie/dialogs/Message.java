package com.wakiedokie.waikiedokie.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.TextView;

import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.database.DBHelper;
import com.wakiedokie.waikiedokie.integration.remote.MyTimerTask;


/**
 * Created by chaovictorshin-deh on 4/27/16.
 */
public class Message {
    DBHelper mydb;
    private User user;
    String current_user_facebook_id;
    String current_user_first_name;
    String current_user_last_name;

    public static void showAlert(final Activity activity, String titleText, String message) {
        TextView title = new TextView(activity);
        title.setText(titleText);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(60);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle(titleText);
        builder.setCustomTitle(title);
        // builder.setIcon(R.drawable.alert_36);

        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent= new Intent(activity, activity.getClass());
                activity.startActivity(intent);
                dialog.cancel();
            }

        });

        AlertDialog alert = builder.create();
        System.out.println("in dialog");
        alert.show();
    }


    public static void showAlertRequestFromOthers(final Activity activity, final String alarm_server_id, final String time, final String owner_name, final String owner_fb_id, String titleText, String message) {
        TextView title = new TextView(activity);
        title.setText(titleText);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(60);


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle(titleText);
        builder.setCustomTitle(title);
        // builder.setIcon(R.drawable.alert_36);

        builder.setMessage(message);

        builder.setCancelable(false);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                (new MyTimerTask(activity)).acceptRequest(activity, alarm_server_id, time, owner_name, owner_fb_id);
                dialog.cancel();
                Intent intent= new Intent(activity, activity.getClass());
                activity.startActivity(intent);

            }

        });
        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                (new MyTimerTask(activity)).denyRequest(activity, owner_name, owner_fb_id);
                dialog.cancel();
                Intent intent= new Intent(activity, activity.getClass());
                activity.startActivity(intent);
            }

        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}

