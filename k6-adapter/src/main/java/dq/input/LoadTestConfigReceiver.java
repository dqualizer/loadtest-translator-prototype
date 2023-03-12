package dq.input;

import dq.adapter.AdaptationManager;
import dq.config.rabbit.Constant;
import dq.dqlang.loadtest.LoadTestConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Imports a dqlang loadtest configuration via RabbitMQ
 */
@Component
public class LoadTestConfigReceiver {

    @Autowired
    private AdaptationManager manager;

    /**
     * Import the loadtest configuration and start the adaptation process
     * @param loadTestConfig Imported loadtest configuration
     */
    @RabbitListener(queues = Constant.LOADTEST_QUEUE)
    public void receive(@Payload LoadTestConfig loadTestConfig) {
        manager.start(loadTestConfig);
    }
}