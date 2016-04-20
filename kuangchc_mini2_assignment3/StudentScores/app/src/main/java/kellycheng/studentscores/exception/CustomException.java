package kellycheng.studentscores.exception;

/**
 * Created by kellycheng on 3/24/16.
 */
public class CustomException extends Exception {
    private String message;

    public CustomException(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return message;
    }
}

