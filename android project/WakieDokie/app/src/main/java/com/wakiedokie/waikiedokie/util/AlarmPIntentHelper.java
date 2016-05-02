package com.wakiedokie.waikiedokie.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wakiedokie.waikiedokie.ui.AlarmConfirmActivity;
import com.wakiedokie.waikiedokie.ui.AlarmStatusActivity;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by chaovictorshin-deh on 5/1/16.
 */
public class AlarmPIntentHelper {

    private static final int PENDING_CODE_OFFSET = 990000;


    public static void addPendingIntent(Activity activity, int alarmID, long timeMillis) {
        AlarmManager am = (AlarmManager)activity.getSystemService(Activity.ALARM_SERVICE);
        int requestCode = PENDING_CODE_OFFSET + alarmID;
        Intent alarmRingIntent = new Intent(activity, AlarmStatusActivity.class);
        alarmRingIntent.putExtra("alarmID", alarmID);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                requestCode, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP,timeMillis,
                pendingIntent);
        makeCountdownToast(activity, timeMillis);
    }

    /* Helper function to make toast that shows how much time till alarm goes off */
    private static void makeCountdownToast(Activity activity, long timeMillis) {

        // Logging stuff
        Calendar now = Calendar.getInstance();
        Long hours = TimeUnit.MILLISECONDS.toHours(
                timeMillis-now.getTimeInMillis());
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(
                timeMillis - now.getTimeInMillis() - TimeUnit.HOURS.toMillis(hours));
        String hoursStr = Long.toString(hours);
        String minutesStr = Long.toString(minutes);
        String toastStr = "Alarm will go off in " + hoursStr + " hrs " + minutesStr + " mins";
        Toast.makeText(activity.getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();

    }


}
