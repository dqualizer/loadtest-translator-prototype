package dq.translator;

import dq.dqlang.mapping.Mapping;
import dq.dqlang.modeling.Modeling;
import dq.input.MappingLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class TranslationManager {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private MappingLoader mappingLoader;

    public void start(Modeling modeling) throws IOException {
        logger.info("Manager ready");
        Mapping mapping = mappingLoader.load();
    }
}