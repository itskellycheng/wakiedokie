package exception;

public class MyException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;

    public MyException(String message) {

        this.message = message;
    }

    @Override
    public String toString() {

        return message;
    }
}