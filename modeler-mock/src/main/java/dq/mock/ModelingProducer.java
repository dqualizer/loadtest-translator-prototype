package dq.mock;

import dq.mock.config.rabbit.Constant;
import dq.mock.config.PathConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Loads the modeling from a local file and sends it to a message-queue
 */
@Component
public class ModelingProducer {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private PathConfig paths;

    /**
     * Method starts as soon as Spring is ready.
     * Sends the modeling to the loadtest-translator
     * @throws IOException
     */
    @EventListener(ApplicationReadyEvent.class)
    public void produce() throws IOException {
        String modeling = this.loadModeling();

        template.convertAndSend(
                Constant.EXCHANGE,
                Constant.KEY,
                modeling
        );
    }

    /**
     * Load modeling from a local file
     * @return String The file content as a String
     * @throws IOException
     */
    private String loadModeling() throws IOException {
        String modelingPath = paths.getModeling();
        String modelingJSON = Files.readString(Paths.get(modelingPath));

        return modelingJSON;
    }
}