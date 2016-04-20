package kellycheng.studentscores.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kellycheng on 3/24/16.
 */
public class DatabaseConnector {
    // database name
    private static final String DATABASE_NAME = "Students2";
    private SQLiteDatabase database; // database object
    private DatabaseOpenHelper databaseOpenHelper; // database helper

    // public constructor for DatabaseConnector
    public DatabaseConnector(Context context)
    {
        System.out.println("In dbc constructor");
        // create a new DatabaseOpenHelper
        databaseOpenHelper =
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    } // end DatabaseConnector constructor

    // open the database connection
    public void open() throws SQLException
    {
        // create or open a database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();
    } // end method open

    // close the database connection
    public void close()
    {
        if (database != null)
            database.close(); // close the database connection
    } // end method close

    // inserts a new student info in the database
    public void insertStudent(String studentid, double q1, double q2, double q3, double q4,
                               double q5)
    {
        ContentValues newStudent = new ContentValues();
        newStudent.put("studentid", studentid);
        newStudent.put("q1", q1);
        newStudent.put("q2", q2);
        newStudent.put("q3", q3);
        newStudent.put("q4", q4);
        newStudent.put("q5", q5);

        open(); // open the database
        database.insert("student", null, newStudent);
        close(); // close the database
    } // end method insertMortgage


    // return a Cursor with all contact information in the database
    public Cursor getAllStudents()
    {
        return database.query("student", new String[] {"_id", "studentid"},
                null, null, null, null, "studentid");
    } // end method getAllStudents


    private class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        // public constructor
        public DatabaseOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        } // end DatabaseOpenHelper constructor

        // creates the mortgage table when the database is created
        @Override
        public void onCreate(SQLiteDatabase db)
        {

            //db.execSQL("DROP TABLE IF EXISTS student");
            System.out.println("blahhhhhhhhhh");
            // query to create a new table named mortgage
            String createQuery = "CREATE TABLE student" +
                    "(_id integer primary key autoincrement," +
                    "studentid TEXT, q1 REAL, q2 REAL," +
                    "q3 REAL," +
                    "q4 TEXT, q5 REAL);";

            db.execSQL(createQuery); // execute the query
        } // end method onCreate

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
        } // end method onUpgrade
    } // end class DatabaseOpenHelper
}

