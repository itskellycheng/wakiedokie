package kellycheng.studentscores.model;

/**
 * Created by kellycheng on 3/31/16.
 */
public class Student {
    private String id;
    private double q1;
    private double q2;
    private double q3;
    private double q4;
    private double q5;

    public Student() {

    }

    public Student(String id, double q1, double q2, double q3, double q4,
                   double q5) {
        super();
        this.id = id;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.q5 = q5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getQ1() {
        return q1;
    }

    public void setQ1(double q1) {
        this.q1 = q1;
    }

    public double getQ2() {
        return q2;
    }

    public void setQ2(double q2) {
        this.q2 = q2;
    }

    public double getQ3() {
        return q3;
    }

    public void setQ3(double q3) {
        this.q3 = q3;
    }

    public double getQ4() {
        return q4;
    }

    public void setQ4(double q4) {
        this.q4 = q4;
    }

    public double getQ5() {
        return q5;
    }

    public void setQ5(double q5) {
        this.q5 = q5;
    }

    public double getQ(int i) {
        switch (i) {
            case 0:
                return q1;
            case 1:
                return q2;
            case 2:
                return q3;
            case 3:
                return q4;
            case 4:
                return q5;
            default:
                return q1;
        }

    }
}
