package poc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HostRetriever {

    @Value("${api.host}")
    private String host;

    public String getHost() {
        return this.host;
    }
}