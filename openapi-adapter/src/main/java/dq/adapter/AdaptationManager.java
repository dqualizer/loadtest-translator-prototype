package dq.adapter;

import dq.dqlang.APISchema;
import dq.exception.AdaptationFailedException;
import dq.input.SchemaLoader;
import dq.output.SchemaProducer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * Manages the whole adaptation process
 * 1. Load an OpenAPI-schema from a running application
 * 2. Adapt the OpenAPI-schema to a dqlang-API-schema
 * 3. Export the adapted API-schema
 */
@Component
public class AdaptationManager {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private SchemaLoader loader;
    @Autowired
    private OpenAPIAdapter adapter;
    @Autowired
    private SchemaProducer producer;

    /**
     * Method starts as soon as Spring is ready.
     * Starts the Adaptation process
     */
    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        try {
            this.adapt();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AdaptationFailedException(e.getMessage());
        }
    }

    private void adapt() throws URISyntaxException, IOException, InterruptedException {
        JSONObject openAPISchema = loader.load();
        logger.info("### OPENAPI SCHEMA LOADED ###");
        APISchema apiSchema = adapter.adapt(openAPISchema);
        logger.info("### OPENAPI SCHEMA ADAPTED ###");
        producer.produce(apiSchema);
        logger.info("### DQLANG API SCHEMA PRODUCED ###");
    }
}