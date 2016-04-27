package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wakiedokie.waikiedokie.R;

import java.io.IOException;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class AlarmStatusActivity extends Activity {
    private MediaPlayer mMediaPlayer;
    private Ringtone mRingtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        Button btn_turn_off = (Button) findViewById(R.id.btn_turn_off);
        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.stop();
//                mRingtone.stop();
                if (mMediaPlayer.isPlaying()) {
                    System.out.println("Playing");
                }
                else {
                    System.out.println("Not Playing");
                }
                Intent intent = new Intent(AlarmStatusActivity.this, AlarmMainActivity.class);
                startActivity(intent);
            }
        });

        playSound(this, getAlarmUri());
    }

    private void playSound(Context context, Uri alert) {
        System.out.println("In playSound");
//        mRingtone = RingtoneManager.getRingtone(context, alert);
//        mRingtone.play();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mMediaPlayer.setLooping(true);
        AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
        try {
            mMediaPlayer.setDataSource(getApplicationContext(), alert);
            System.out.println(alert);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            if (mMediaPlayer.isPlaying()) {
                    System.out.println("Playing");
            }
            else {
                System.out.println("Not Playing");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        mMediaPlayer = new MediaPlayer();
//        try {
//            mMediaPlayer.setDataSource(context, alert);
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            final AudioManager audioManager = (AudioManager) context
//                    .getSystemService(Context.AUDIO_SERVICE);
//            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//                if (mMediaPlayer.isPlaying()) {
//                    System.out.println("Playing");
//                }
//                else {
//                    System.out.println("Not Playing");
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("OOPS");
//        }
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
