package kellycheng.studentscores.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kellycheng.studentscores.database.DatabaseConnector;
import kellycheng.studentscores.model.Student;

/**
 * Created by kellycheng on 3/31/16.
 */
public class ReadScores {

    public ReadScores(){}
    public ReadScores(DatabaseConnector dbc){
        this.dbc = dbc;
    }

    private DatabaseConnector dbc;
    public ArrayList<Student> students = new ArrayList<Student>();

    /* readData - takes in name of text file and reads it line by line */
    public void readData(InputStream is) {

        BufferedReader buff = null;
        boolean eof = false;
        String line = null;
//        myFile = new File(filename);
//        FileReader file = null;
//        try {
//            file = new FileReader(myFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        InputStreamReader isr = new InputStreamReader(is);
        buff = new BufferedReader(isr);
        eof = false;
        //read first line
        try {
            while (!eof) {
                System.out.println("Reading line");
                line = buff.readLine();
                if (line == null) {
                    System.out.println("EOF");
                    eof = true;
                }
                else {
                    parseLine(line);
                }
            }
            buff.close();
            System.out.println("Done reading");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line) {
        System.out.println("Parse line");
        String[] arr=line.split(",");

        Student stu = new Student(arr[0],
                Double.parseDouble(arr[1]),
                Double.parseDouble(arr[2]),
                Double.parseDouble(arr[3]),
                Double.parseDouble(arr[4]),
                Double.parseDouble(arr[5]));

        System.out.println(arr[0]);

        dbc.insertStudent(arr[0],
                Double.parseDouble(arr[1]),
                Double.parseDouble(arr[2]),
                Double.parseDouble(arr[3]),
                Double.parseDouble(arr[4]),
                Double.parseDouble(arr[5]));

        students.add(stu);
    }



}
