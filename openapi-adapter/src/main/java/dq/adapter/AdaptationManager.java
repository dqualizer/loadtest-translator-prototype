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

@Component
public class AdaptationManager {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private SchemaLoader loader;
    @Autowired
    private OpenAPIAdapter adapter;
    @Autowired
    private SchemaProducer producer;

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