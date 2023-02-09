package poc.loadtest.exception;

import java.util.Map;

public class NoReferenceFoundException extends RuntimeException {

    public NoReferenceFoundException(Map<String, String> referenceMap) {
        super("No referenc could be found: " + referenceMap);
    }
}
