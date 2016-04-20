package kellycheng.musicartist.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import kellycheng.musicartist.R;

/**
 * Created by kellycheng on 3/31/16.
 */
public class VideoFragment extends Fragment {

    private VideoView myVideoView1, myVideoView2;
    private MediaController mediaControls1 = null;
    private MediaController mediaControls2 = null;
    private boolean videoFlag = true;
    View rootView;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_video, container, false);

        Button switchBtn = (Button) rootView.findViewById(R.id.switch_btn);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                videoFlag = !videoFlag;
                switchVideo();
                System.out.println("Start playing 1");
            }
        });



        //set the media controller buttons
        if (mediaControls1 == null) {
            mediaControls1 = new MediaController(getActivity());
        }

        try {
            if (videoFlag) {
                //initialize the VideoView
                myVideoView1 = (VideoView) rootView.findViewById(R.id.video_view1);
                //set the media controller in the VideoView
                myVideoView1.setMediaController(mediaControls1);
                //set the uri of the video to be played
                myVideoView1.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.vid1));
            }
            else {
                myVideoView1 = (VideoView) rootView.findViewById(R.id.video_view1);
                //set the media controller in the VideoView
                myVideoView1.setMediaController(mediaControls2);
                //set the uri of the video to be played
                myVideoView1.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.vid2));

            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        return rootView;
    }

    public void switchVideo() {
        if (videoFlag) {
            //initialize the VideoView
            myVideoView1 = (VideoView) rootView.findViewById(R.id.video_view1);
            //set the media controller in the VideoView
            myVideoView1.setMediaController(mediaControls1);
            //set the uri of the video to be played
            myVideoView1.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.vid1));
        }
        else {
            myVideoView1 = (VideoView) rootView.findViewById(R.id.video_view1);
            //set the media controller in the VideoView
            myVideoView1.setMediaController(mediaControls1);
            //set the uri of the video to be played
            myVideoView1.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.vid2));

        }
    }
}
