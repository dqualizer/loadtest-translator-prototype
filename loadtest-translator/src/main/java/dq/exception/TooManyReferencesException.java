package dq.exception;

import java.util.Set;

public class TooManyReferencesException extends RuntimeException {

    public TooManyReferencesException(Set<String> keySet) {
        super("The following Set has too many references: " + keySet);
    }
}