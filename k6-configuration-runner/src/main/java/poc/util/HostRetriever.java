package poc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HostRetriever {

    @Value("${api.host}")
    private String apiHost;
    @Value("${influx.host}")
    private String influxHost;

    public String getAPIHost() {
        return this.apiHost;
    }

    public String getInfluxHost() {
        return this.influxHost;
    }
}