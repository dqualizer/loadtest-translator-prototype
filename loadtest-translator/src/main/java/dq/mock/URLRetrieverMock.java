package dq.mock;

import org.springframework.stereotype.Component;

/**
 * Simulates a service that retrieves the ip address and port from a logical url.
 * For example for microservices running in the cloud or in a cluster
 */
@Component
public class URLRetrieverMock {

    public String retrieve(String url) {
        return url;
    }
}