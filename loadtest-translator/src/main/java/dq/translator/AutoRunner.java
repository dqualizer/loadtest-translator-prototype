package dq.translator;

import dq.dqlang.Mapping;
import dq.exception.RunnerFailedException;
import dq.input.MappingLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class AutoRunner {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private MappingLoader mappingLoader;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        logger.info("### TRANSLATOR STARTED ###");
        try {
            this.startTranslator();
        } catch (Exception e) {
            logger.severe("### FAILED ###");
            e.printStackTrace();
            throw new RunnerFailedException(e.getMessage());
        }
    }

    private void startTranslator() throws IOException {
        Mapping mapping = mappingLoader.load();
    }
}