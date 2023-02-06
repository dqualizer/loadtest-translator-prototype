package dq.exception;

public class EnvironmentNotFoundException extends RuntimeException {

    public EnvironmentNotFoundException(String environment) {
        super("Environment '" + environment + "' was not found");
    }
}