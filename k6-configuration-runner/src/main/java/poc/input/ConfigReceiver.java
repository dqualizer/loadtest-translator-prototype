package poc.input;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import poc.config.rabbit.Constant;
import poc.dqlang.Config;
import poc.loadtest.ConfigRunner;

/**
 * Imports a k6 loadtest configuration via RabbitMQ
 */
@Component
public class ConfigReceiver {

    @Autowired
    private ConfigRunner runner;

    /**
     * Import the k6 configuration and start the configuration runner
     * @param config An inofficial k6 configuration
     */
    @RabbitListener(queues = Constant.QUEUE)
    public void receive(@Payload Config config) {
        runner.start(config);
    }
}