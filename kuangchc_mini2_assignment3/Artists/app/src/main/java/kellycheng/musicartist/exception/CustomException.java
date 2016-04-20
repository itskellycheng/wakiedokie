package kellycheng.musicartist.exception;

/**
 * Created by kellycheng on 3/31/16.
 */
public class CustomException {
    private String message;

    public CustomException(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return message;
    }
}
