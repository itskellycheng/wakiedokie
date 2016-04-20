package kellycheng.musicartist.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kellycheng.musicartist.R;

/**
 * Created by kellycheng on 3/31/16.
 */
public class PicFragment extends Fragment {

    public PicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pic, container, false);
        return rootView;
    }


}
