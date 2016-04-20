package kellycheng.musicartist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import kellycheng.musicartist.R;

public class MainActivity extends AppCompatActivity {

    private Button button_main,button_music,button_video,button_pics,button_mail;
    private Intent myIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initial fragment
        if (findViewById(R.id.fragment_container) != null) {

            // Create a new Fragment to be placed in the activity layout
            MainActivityFragment mainFragment = new MainActivityFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mainFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mainFragment).commit();
        }

//        TextView site_link = (TextView) findViewById(R.id.mainpage_site_Textview);
//        //TextView twitter_link = (TextView)findViewById(R.id.mainpage_twitter_Textview);
//
//        site_link.setMovementMethod(LinkMovementMethod.getInstance());
//        String text = "<a href='www.britneyspears.com'>Website</a>";
//        site_link.setText(Html.fromHtml(text));

        button_main = (Button)findViewById(R.id.button_main);
        button_music = (Button)findViewById(R.id.button_music);
        button_video = (Button)findViewById(R.id.button_video);
        button_pics = (Button)findViewById(R.id.button_pictures);
        button_mail = (Button)findViewById(R.id.button_mail);

        button_music.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                // Perform action on click
                MusicFragment newFragment = new MusicFragment();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                System.out.println("Clicked");
            }
        });
        button_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                // Perform action on click
                VideoFragment newFragment = new VideoFragment();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                System.out.println("Clicked");
            }
        });
        button_pics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                // Perform action on click
                PicFragment newFragment = new PicFragment();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                System.out.println("Clicked");
            }
        });
        button_mail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                // Perform action on click
                MailFragment newFragment = new MailFragment();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                System.out.println("Clicked");
            }
        });
        button_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                // Perform action on click
                MainActivityFragment newFragment = new MainActivityFragment();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                System.out.println("Clicked");
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
