package dq.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dq.config.rabbit.Constant;
import dq.dqlang.modeling.Modeling;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ModelingReceiver {

    @RabbitListener(queues = Constant.MODELING_QUEUE)
    public Modeling receive(@Payload String modelingJSON) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //Modeling modeling = objectMapper.readValue(modelingJSON, Modeling.class);

        //return modeling;
        System.out.println("#######################################################");
        return null;
    }
}