package kellycheng.studentscores.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kellycheng.studentscores.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResultsFragment extends Fragment {

    public ResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        return rootView;
    }

    public void setText(String url) {
        TextView view = (TextView) getView().findViewById(R.id.resultsText);
        view.setText(url);
    }
}
