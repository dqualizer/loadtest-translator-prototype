package dq.exception;

public class IDNotFoundException extends RuntimeException {

    public IDNotFoundException(String ID) {
        super("Unknown ID: " + ID);
    }
}