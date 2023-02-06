package dq.input;

import dq.rabbit.Constant;
import dq.dqlang.loadtest.LoadTestConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Imports a dqlang loadtest configuration via RabbitMQ
 */
@Component
public class LoadTestConfigReceiver {

    @RabbitListener(queues = Constant.LOADTEST_QUEUE)
    public void receive(@Payload LoadTestConfig loadTestConfig) {
        System.out.println("CONFIGURATION RECEIVED: " + loadTestConfig);
    }
}