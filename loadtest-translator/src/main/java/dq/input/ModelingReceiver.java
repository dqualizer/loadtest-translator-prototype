package dq.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.config.rabbit.Constant;
import dq.dqlang.modeling.Modeling;
import dq.exception.InvalidModelingSchemaException;
import dq.translator.TranslationManager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Import the Modeling via RabbitMQ
 */
@Component
public class ModelingReceiver {

    @Autowired
    private TranslationManager manager;

    @RabbitListener(queues = Constant.MODELING_QUEUE)
    public void receive(@Payload String modelingJSON) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Modeling modeling;

        try {
            modeling = objectMapper.readValue(modelingJSON, Modeling.class);
        } catch (Exception e) {
            throw new InvalidModelingSchemaException(e.getMessage());
        }

        manager.start(modeling);
    }
}