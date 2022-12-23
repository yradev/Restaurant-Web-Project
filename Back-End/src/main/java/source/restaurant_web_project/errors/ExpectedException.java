package source.restaurant_web_project.errors;

public class ExpectedException extends RuntimeException{
    public ExpectedException() {
        super();
    }

    public ExpectedException(String message) {
        super(message);
    }

    public ExpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpectedException(Throwable cause) {
        super(cause);
    }

    protected ExpectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
