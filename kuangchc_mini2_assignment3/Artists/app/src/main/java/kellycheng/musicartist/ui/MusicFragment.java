package kellycheng.musicartist.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import kellycheng.musicartist.R;

/**
 * Created by kellycheng on 3/31/16.
 */
public class MusicFragment extends Fragment {
    public MusicFragment() {
    }

    // media player
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);



        Button playBtn1 = (Button) rootView.findViewById(R.id.play_btn1);
        Button playBtn2 = (Button) rootView.findViewById(R.id.play_btn2);
        Button stopBtn1 = (Button) rootView.findViewById(R.id.stop_btn1);
        Button stopBtn2 = (Button) rootView.findViewById(R.id.stop_btn2);


        playBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.song1);
                mediaPlayer.start();
                System.out.println("Start playing 1");
            }
        });
        stopBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //mediaPlayer = MediaPlayer.create(getActivity(),R.raw.song1);
                mediaPlayer.stop();
                System.out.println("Stop playing 1");
            }
        });

        playBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.song2);
                mediaPlayer.start();
                System.out.println("Start playing 2");
            }
        });
        stopBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //mediaPlayer = MediaPlayer.create(getActivity(),R.raw.song1);
                mediaPlayer.stop();
                System.out.println("Stop playing 2");
            }
        });

        return rootView;
    }
}
