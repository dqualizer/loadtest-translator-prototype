package dq.output;

import dq.dqlang.APISchema;
import dq.rabbit.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Exports a dqualizer API schema via RabbitMQ
 */
@Component
public class SchemaProducer {

    @Autowired
    private RabbitTemplate template;

    public String produce(APISchema schema) {
        template.convertAndSend(
                Constant.EXCHANGE,
                Constant.KEY,
                schema
        );

        return "API SCHEMA WAS PRODUCED";
    }
}