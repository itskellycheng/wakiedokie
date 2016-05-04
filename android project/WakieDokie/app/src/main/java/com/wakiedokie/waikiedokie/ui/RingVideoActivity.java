package com.wakiedokie.waikiedokie.ui;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.wakiedokie.waikiedokie.R;
import com.wakiedokie.waikiedokie.integration.remote.Connection;

/**
 * Created by chaovictorshin-deh on 4/14/16.
 */
public class RingVideoActivity extends Activity {
    ProgressDialog pDialog;
    VideoView videoView;
    String myVideoFileName;
    private long enqueue;
    private DownloadManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_video);

        videoView = (VideoView)findViewById(R.id.ring_video_view);

        myVideoFileName = "VID_20160503_214813.mp4";
        String VideoURL = Connection.DOMAIN + Connection.VIDEO_STREAM_SERVLET + myVideoFileName;

//        String vidAddress = "http://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
//        String vidAddress = "http://10.0.0.169:8081/AndroidAppServlet/video/VID_20160503_214813.mp4";
//        Uri vidUri = Uri.parse(vidAddress);
//        videoView.setVideoURI(vidUri);
//        videoView.start();

        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse("http://10.0.0.169:8081/AndroidAppServlet/video/VID_20160503_214813.mp4"));
        enqueue = dm.enqueue(request);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

//                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            Uri vidUri = Uri.parse(uriString);
                            try {
                                // Start the MediaController
                                MediaController mediacontroller = new MediaController(RingVideoActivity.this);
                                mediacontroller.setAnchorView(videoView);
                                // Get the URL from String VideoURL
                                videoView.setMediaController(mediacontroller);

                            } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                                e.printStackTrace();
                            }
                            videoView.setVideoURI(vidUri);
                            videoView.start();
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

//        // Create a progressbar
//        pDialog = new ProgressDialog(RingVideoActivity.this);
//        // Set progressbar title
//        pDialog.setTitle("Wake up video");
//        // Set progressbar message
//        pDialog.setMessage("Buffering...");
//        pDialog.setIndeterminate(false);
////        pDialog.setCancelable(false);
//        // Show progressbar
//        pDialog.show();
//
//        try {
//            // Start the MediaController
//            MediaController mediacontroller = new MediaController(this);
//            mediacontroller.setAnchorView(videoView);
//            // Get the URL from String VideoURL
//            Uri video = Uri.parse(VideoURL);
//            videoView.setMediaController(mediacontroller);
//            videoView.setVideoURI(video);
//
//        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
//
//        videoView.requestFocus();
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            // Close the progress bar and play the video
//            public void onPrepared(MediaPlayer mp) {
//                pDialog.dismiss();
//                videoView.start();
//            }
//        });

    }

    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }
}
