package dq.exception;

import java.util.Set;

public class TooManyReferencesException extends RuntimeException {

    public TooManyReferencesException(Set<String> parameterKeys, Set<String> payloadKeys) {
        super("Parameter Set: " + parameterKeys + " ### Payload Set: " + payloadKeys);
    }
}