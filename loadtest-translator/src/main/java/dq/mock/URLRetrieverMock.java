package dq.mock;

import org.springframework.stereotype.Component;

@Component
public class URLRetrieverMock {

    public String retrieve(String url) {
        return url;
    }
}