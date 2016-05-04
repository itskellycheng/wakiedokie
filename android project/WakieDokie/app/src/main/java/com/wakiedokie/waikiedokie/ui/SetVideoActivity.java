package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.database.DBHelper;
import com.wakiedokie.waikiedokie.integration.remote.EditAlarmTypeHelper;
import com.wakiedokie.waikiedokie.integration.remote.UploadVideo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class SetVideoActivity extends Activity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final String TAG = "SetVideoActivity";
    private Uri fileUri = null;
    private VideoView mVideoView;
    private MediaController mediaControls = null;
    private FrameLayout videoContainer;
    UploadVideo uploadVideo;
    private String ownerID;
    private String user2ID;
    private int alarmID;
    private String my_fb_id;
    private Activity activity;
    private DBHelper dbHelper;
    private EditAlarmTypeHelper eatHelper;
    private String serverAlarmID;
    private String videoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_video);
        dbHelper = new DBHelper(this);
        Intent thisIntent = getIntent();
        alarmID = thisIntent.getIntExtra("alarmID", -1);
        serverAlarmID = dbHelper.getServerAlarmId(alarmID);
        activity = this;
        my_fb_id = dbHelper.getMyIDFromMeTable();


        videoContainer = (FrameLayout)findViewById(R.id.video_container);

        takeVideo();

        if (fileUri != null) {
            previewVideo();
        }

        Button btn = (Button) findViewById(R.id.button_capture);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

        Button btn_save_video = (Button) findViewById(R.id.btn_save_video);
        btn_save_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SetVideoActivity.this, "Video will play when your buddy's alarm rings",
                        Toast.LENGTH_LONG).show();
                uploadVideo = new UploadVideo();
                try {
                    //hardcode for testing
                    ownerID = "iamtheowner";
                    user2ID = "iamuser2";
                    uploadVideo.run(fileUri.getEncodedPath(), ownerID, user2ID);
                    Log.d(TAG, "video sent");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                eatHelper = new EditAlarmTypeHelper(activity, alarmID, DBHelper.ALARM_TYPE_VIDEO);
                eatHelper.sendEditAlarmTypeToServer();
                dbHelper.editAlarmType(alarmID, my_fb_id, DBHelper.ALARM_TYPE_VIDEO);
                dbHelper.printAllAlarms();
                Intent intent = new Intent(SetVideoActivity.this, AlarmMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void takeVideo() {
        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
//        System.out.println(fileUri);

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    public void previewVideo() {
        mVideoView = new VideoView(SetVideoActivity.this);
        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        mVideoView.setMediaController(mediaControls);
        mVideoView.setVideoURI(fileUri);
        // create and place a thumbnail for the start state
        Bitmap thumbAsBitmap = null;
        BitmapDrawable thumbAsDrawable = null;
        thumbAsBitmap = ThumbnailUtils.createVideoThumbnail(fileUri.getEncodedPath(), MediaStore.Images.Thumbnails.MINI_KIND);
        thumbAsDrawable = new BitmapDrawable(thumbAsBitmap);
        mVideoView.setBackgroundDrawable(thumbAsDrawable);
        videoContainer.addView(mVideoView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
//                Toast.makeText(this, "Image saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Image saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
//                Toast.makeText(this, "Video saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Image saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "WakieDokie");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("WakieDokie", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            if (serverAlarmID.equals("-1")) {
                Toast.makeText(this, "WARNING: serverAlarmID is -1", Toast.LENGTH_SHORT).show();
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "WAKIE_VID_"+ serverAlarmID + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
