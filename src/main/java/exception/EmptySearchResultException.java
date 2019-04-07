package exception;

public class EmptySearchResultException extends Exception {
    private String m;

    public EmptySearchResultException() {
    }

    public EmptySearchResultException(String m) {
        super(m);
    }
}
