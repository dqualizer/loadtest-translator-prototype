package dq.exception;

import java.util.Set;

public class TooManyReferencesException extends RuntimeException {

    public TooManyReferencesException(Set<String> urlParameterKeys, Set<String> requestParameterKeys, Set<String> payloadKeys) {
        super("### URLParameter Set: " + urlParameterKeys +
                " ### RequestParameter Set: " + requestParameterKeys +
                " ### Payload Set: " + payloadKeys);
    }
}