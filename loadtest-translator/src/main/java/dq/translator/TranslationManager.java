package dq.translator;

import dq.dqlang.loadtest.LoadTestConfig;
import dq.dqlang.mapping.Mapping;
import dq.dqlang.modeling.Modeling;
import dq.input.MappingLoader;
import dq.output.LoadTestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Manages the whole translation process
 * 1. Load a mapping for the Bounded Context of the modeling
 * 2. Translate the modeling with the help of the mapping to a loadtest configuration
 * 3. Export the loadtest configuration
 */
@Component
public class TranslationManager {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private MappingLoader mappingLoader;
    @Autowired
    private LoadTestTranslator translator;
    @Autowired
    private LoadTestProducer loadTestProducer;

    public void start(Modeling modeling) throws IOException {
        String context = modeling.getContext();
        Mapping mapping = mappingLoader.load(context);
        logger.info("### MAPPING SUCESSFULLY LOADED ###");
        LoadTestConfig loadTestConfig = translator.translate(modeling, mapping);
        logger.info("### TRANSLATION FINISHED ###");
        loadTestProducer.produce(loadTestConfig);
        logger.info("### LOADTEST CONFIGURATION PRODUCED ###");
    }
}