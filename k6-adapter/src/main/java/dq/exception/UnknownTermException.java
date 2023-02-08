package dq.exception;

public class UnknownTermException extends RuntimeException {

    public UnknownTermException(String term) {
        super("Unknown term: " + term);
    }
}