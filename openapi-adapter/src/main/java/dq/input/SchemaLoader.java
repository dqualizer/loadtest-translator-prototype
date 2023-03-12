package dq.input;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Loads an OpenAPI-schema from a running application
 */
@Component
public class SchemaLoader {

    @Value("${api.port:9000}")
    private String apiPort;

    /**
     * Loads an Open-API-schema from localhost:<apiPort>/v3/api-docs
     * @return A JSONObject with the OpenAPI-Schema
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public JSONObject load() throws URISyntaxException, IOException, InterruptedException {
        String serverURI = "http://127.0.0.1:" + apiPort +"/v3/api-docs";
        URI uri = new URI(serverURI);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if(statusCode != 200) throw new RuntimeException("Loading schema failed - Status not 200, but " + statusCode);

        String schema = response.body();
        JSONObject schemaJSON = new JSONObject(schema);
        return schemaJSON;
    }
}