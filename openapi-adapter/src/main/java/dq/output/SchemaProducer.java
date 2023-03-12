package dq.output;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dq.config.PathConfig;
import dq.dqlang.APISchema;
import dq.config.rabbit.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Exports a dqlang API-schema via RabbitMQ
 */
@Component
public class SchemaProducer {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private PathConfig paths;

    /**
     * Sends the adapted API-Schema to a queue (normally to dqedit)
     * @param schema Adapted API-schema
     * @return String (only for RabbitMQ)
     */
    public String produce(APISchema schema) {
        this.writeToFile(schema);
        template.convertAndSend(
                Constant.EXCHANGE,
                Constant.KEY,
                schema
        );

        return "API SCHEMA WAS PRODUCED";
    }

    /**
     * Write the adapted API-schema to a local file.
     * Only needed for development
     * @param schema Adapted API-schema
     */
    private void writeToFile(APISchema schema) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        String outputPath = paths.getOutput();

        try {
            writer.writeValue(new File(outputPath), schema);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}