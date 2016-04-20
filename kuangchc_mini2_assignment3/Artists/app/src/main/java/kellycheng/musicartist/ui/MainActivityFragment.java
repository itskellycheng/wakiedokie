package kellycheng.musicartist.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import kellycheng.musicartist.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        TextView text1 = (TextView) rootView.findViewById(R.id.mainpage_site_Textview);
        TextView text2 = (TextView) rootView.findViewById(R.id.mainpage_twitter_Textview);

        text1.setText(
                Html.fromHtml(
                        "<a href='www.britneyspears.com'>Website</a> "));
        text1.setMovementMethod(LinkMovementMethod.getInstance());

        text2.setText(
                Html.fromHtml(
                        "<a href='https://twitter.com/britneyspears'>Twitter</a> "));
        text2.setMovementMethod(LinkMovementMethod.getInstance());

        return rootView;
    }
}
