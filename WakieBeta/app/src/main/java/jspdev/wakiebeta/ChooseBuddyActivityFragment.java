package jspdev.wakiebeta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chaovictorshin-deh on 4/4/16.
 */
public class ChooseBuddyActivityFragment extends Fragment{
    public ChooseBuddyActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_wakie_buddy, container, false);
    }

    /* Intent intent = new Intent(getActivity(), NotificaionActivity.class);
       startActivity(intent);*/
}
