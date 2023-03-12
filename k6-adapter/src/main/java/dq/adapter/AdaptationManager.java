package dq.adapter;

import dq.dqlang.k6.K6Config;
import dq.dqlang.loadtest.LoadTestConfig;
import dq.output.K6ConfigProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Manages the whole adaptation process
 * 1. Adapt the imported loadtest configuration to a k6-configuration
 * 2. Export the k6-configuration
 */
@Component
public class AdaptationManager {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private K6Adapter adapter;
    @Autowired
    private K6ConfigProducer producer;

    public void start(LoadTestConfig loadTestConfig) {
        K6Config k6Config = adapter.adapt(loadTestConfig);
        logger.info("### CONFIGURATION ADAPTED ###");
        producer.produce(k6Config);
        logger.info("### k6 CONFIGURATION PRODUCED ###");
    }
}