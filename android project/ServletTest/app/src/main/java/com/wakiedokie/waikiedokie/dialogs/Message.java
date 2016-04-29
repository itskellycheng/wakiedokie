package com.wakiedokie.waikiedokie.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.model.User;
import com.wakiedokie.waikiedokie.ui.AlarmMainActivity;
import com.wakiedokie.waikiedokie.util.database.DBHelper;
import com.wakiedokie.waikiedokie.util.timer.MyTimerTask;


/**
 * Created by chaovictorshin-deh on 4/27/16.
 */
public class Message {
    DBHelper mydb;
    private User user;
    String current_user_facebook_id;
    String current_user_first_name;
    String current_user_last_name;

    public static void showAlert(Activity activity, String titleText, String message) {
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
                dialog.cancel();

            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public static void showAlertRequestFromOthers(final Activity activity, final String time, final String owner_name, final String owner_fb_id, String titleText, String message) {
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
                (new MyTimerTask(activity)).acceptRequest(activity, time, owner_name, owner_fb_id);
                dialog.cancel();

            }

        });
        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                (new MyTimerTask(activity)).denyRequest(activity, owner_name, owner_fb_id);
                dialog.cancel();
            }

        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}

