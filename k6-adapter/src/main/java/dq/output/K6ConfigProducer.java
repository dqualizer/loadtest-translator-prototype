package dq.output;

import dq.dqlang.k6.K6Config;
import dq.config.rabbit.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Exports the k6 loadtest configuration via RabbitMQ
 */
@Component
public class K6ConfigProducer {

    @Autowired
    private RabbitTemplate template;

    public String produce(K6Config k6Config) {
        template.convertAndSend(
                Constant.K6_EXCHANGE,
                Constant.POST_KEY,
                k6Config
        );

        return "K6 LOADTEST CONFIGURATION WAS PRODUCED";
    }
}