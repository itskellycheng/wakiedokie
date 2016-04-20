package kellycheng.studentscores.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kellycheng.studentscores.R;
import kellycheng.studentscores.database.DatabaseConnector;
import kellycheng.studentscores.model.Statistics;
import kellycheng.studentscores.model.Student;
import kellycheng.studentscores.util.ReadScores;

import static kellycheng.studentscores.R.*;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnItemSelectedListener {

    Context context = this;
    Statistics stats = null;
    public ArrayList<Student> students = new ArrayList<Student>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        // read txt file input
        DatabaseConnector dbc = new DatabaseConnector(this);
        ReadScores readScores = new ReadScores(dbc);
        AssetManager am = context.getAssets();
        InputStream is = null;

        try {
            System.out.println("blah");
            is = am.open("scores.txt");
            //descriptor = context.getAssets().openFd("scores.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (is == null)
            System.out.println("boohoohoo");
        else {
//            FileReader file = new FileReader(descriptor.getFileDescriptor());
            System.out.println("about to read data");
                    readScores.readData(is);
            this.students = readScores.students;
            System.out.println(this.students);
            stats = new Statistics(students);
        }


    }

    @Override
    public void onRssItemSelected(String link) {
        ResultsFragment fragment = (ResultsFragment) getSupportFragmentManager()
                .findFragmentById(id.results_fragment);
        if (fragment==null)
            System.out.println("crycry");
        String str = stats.getString(students);
        System.out.println(str);
        fragment.setText(str);
    }
}
