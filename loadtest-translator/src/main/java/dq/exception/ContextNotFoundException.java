package dq.exception;

public class ContextNotFoundException extends RuntimeException {

    public ContextNotFoundException(String context) {
        super("Context '" + context + "' was not found");
    }
}