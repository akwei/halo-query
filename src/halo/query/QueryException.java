package halo.query;

public class QueryException extends Exception {

    private static final long serialVersionUID = -8333257204418273331L;

    public QueryException() {
    }

    public QueryException(String arg0) {
        super(arg0);
    }

    public QueryException(Throwable arg0) {
        super(arg0);
    }

    public QueryException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
