package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.util.database.DBHelper;
import com.wakiedokie.waikiedokie.util.userio.WakeUpHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mMediaPlayer = new MediaPlayer();
        playSound(this, getAlarmUri(), mMediaPlayer);
        dbHelper = new DBHelper(this);
        am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);

        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);

        final WakeUpHelper wuHelper = new WakeUpHelper(this, alarmID, mMediaPlayer);

        Button btn_do_task = (Button)findViewById(R.id.btn_do_task);
        btn_do_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmStatusActivity.this, RingShakeActivity.class);
                startActivity(intent);
            }
        });

        final Button btn_turn_off = (Button) findViewById(R.id.btn_turn_off);
        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_turn_off.setClickable(false);

                // instead of stopping the alarm, send message to server and check response: 1. one is awake 2. both awake
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
        });


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
