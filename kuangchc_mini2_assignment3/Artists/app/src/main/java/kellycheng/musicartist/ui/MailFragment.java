package kellycheng.musicartist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import kellycheng.musicartist.R;

/**
 * Created by kellycheng on 3/31/16.
 */
public class MailFragment extends Fragment {

    private EditText nameET, emailET, subjectET, contentET;
    private Button submit_btn;

    public MailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mail, container, false);

        // get Edit Text
        nameET = (EditText) rootView.findViewById(R.id.mail_name);
        emailET = (EditText) rootView.findViewById(R.id.mail_email);
        subjectET = (EditText) rootView.findViewById(R.id.mail_subject);

        // get button
        submit_btn = (Button) rootView.findViewById(R.id.mail_btn);
        // Set the Listener
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String subject = subjectET.getText().toString();


                    // enable default Gmail activity to send the mail
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "kuangchc@andrew.cmu.edu" });
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi this is" + name + "I wanna join your fan mailing list");
                    startActivity(emailIntent);
            }
        });



        return rootView;
    }
}
