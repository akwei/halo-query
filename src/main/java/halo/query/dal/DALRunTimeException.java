package halo.query.dal;

public class DALRunTimeException extends RuntimeException {

    public DALRunTimeException(String message) {
        super(message);
    }

    public DALRunTimeException(Throwable cause) {
        super(cause);
    }

    public DALRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
