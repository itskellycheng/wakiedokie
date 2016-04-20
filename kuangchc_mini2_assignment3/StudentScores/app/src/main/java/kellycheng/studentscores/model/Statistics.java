package kellycheng.studentscores.model;

import java.util.ArrayList;

/**
 * Created by kellycheng on 3/31/16.
 */
public class Statistics {
    StringBuilder sb = new StringBuilder();
    private double[] highScore = new double[5];
    private double[] lowScore = new double[5];
    private double[] avgScore = new double[5];
    private double[] sum = new double[5];


    public Statistics(ArrayList<Student> students) {
        //set initial values to the first student
        for (int q=0; q<5; q++) {
            highScore[q] = students.get(0).getQ(q);
            lowScore[q] = students.get(0).getQ(q);
        }
        calculate(students);
    }

    private void calculate(ArrayList<Student> students) {
        for (int q=0; q<5; q++) {
            for (int i=0; i<students.size(); i++) {
                double score = students.get(i).getQ(q);
                sum[q]+=score;
                if (score > highScore[q]) {
                    highScore[q] = score;
                }
                if (score < lowScore[q]) {
                    lowScore[q] = score;
                }
            }
        }

        for (int q=0; q<5; q++) {
            avgScore[q]=sum[q]/5;
        }

    }

    public String getString(ArrayList<Student> students) {
        sb.append("Highscore"+ "\t");
        for (int q=0; q<5; q++) {
            sb.append(highScore[q] + "\t");
        }
        sb.append("\n");
        sb.append("Lowscore"+ "\t");
        for (int q=0; q<5; q++) {
            sb.append(lowScore[q] + "\t");
        }
        sb.append("\n");
        sb.append("avgscore"+ "\t");
        for (int q=0; q<5; q++) {
            sb.append(avgScore[q] + "\t");
        }
        return sb.toString();
    }

}
