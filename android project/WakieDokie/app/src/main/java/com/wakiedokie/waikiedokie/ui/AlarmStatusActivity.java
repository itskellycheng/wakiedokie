package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.database.DBHelper;
import com.wakiedokie.waikiedokie.integration.remote.GetAlarmTypeHelper;
import com.wakiedokie.waikiedokie.integration.remote.WakeUpHelper;

import java.io.IOException;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmStatusActivity extends Activity {
    private static final String TAG = "wakiebooboo";
    private MediaPlayer mMediaPlayer;
    private int alarmID;
    private static final int PENDING_CODE_OFFSET = 990000;
    private AlarmManager am;
    private DBHelper dbHelper;
    private Activity activity;
    private WakeUpHelper wuHelper;
    private GetAlarmTypeHelper gatHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        activity = this;
        mMediaPlayer = new MediaPlayer();
        playSound(this, getAlarmUri(), mMediaPlayer);
        dbHelper = new DBHelper(this);
        am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);

        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);

        wuHelper = new WakeUpHelper(this, alarmID, mMediaPlayer);
        final Button btn_turn_off = (Button) findViewById(R.id.btn_turn_off);
        final Button btn_do_task = (Button)findViewById(R.id.btn_do_task);
        gatHelper = new GetAlarmTypeHelper(this, alarmID, btn_do_task, btn_turn_off, wuHelper);
        gatHelper.getMyAlarmTypeFromServer();





    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String toastStr = "Yay! You've finished your task!! Now Try everything you can to wake up your buddy";
        Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_LONG).show();
        wuHelper.sendWakeUpMessageToServer();

        if (mMediaPlayer.isPlaying()) {
            System.out.println("Playing");
        } else {
            if (alarmID < 0) {
                Log.d(TAG, "alarmID incorrect");
            } else {
                dbHelper.setAlarmToInactive(alarmID);
                int requestCode = PENDING_CODE_OFFSET + alarmID;
                Intent alarmRingIntent = new Intent(AlarmStatusActivity.this, AlarmStatusActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(AlarmStatusActivity.this,
                        requestCode, alarmRingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                pendingIntent.cancel();
                am.cancel(pendingIntent);
            }
        }

    }

    private void playSound(Context context, Uri alert, MediaPlayer mMediaPlayer) {
        System.out.println("In playSound");
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mMediaPlayer.setLooping(true);
        AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume / 4, 0);
        try {
            mMediaPlayer.setDataSource(getApplicationContext(), alert);
            System.out.println(alert);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            if (mMediaPlayer.isPlaying()) {
                System.out.println("Playing");
            } else {
                System.out.println("Not Playing");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        System.out.println("alert!!!");
        System.out.println(alert);
        return alert;
    }

}
