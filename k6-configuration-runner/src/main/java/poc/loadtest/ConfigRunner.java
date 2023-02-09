package poc.loadtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.config.PathConfig;
import poc.dqlang.Config;
import poc.dqlang.LoadTest;
import poc.util.ProcessLogger;

import java.util.LinkedHashSet;
import java.util.logging.Logger;

@Component
public class ConfigRunner {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private PathConfig paths;
    @Autowired
    private ScriptMapper mapper;
    @Autowired
    private ScriptWriter writer;
    @Autowired
    private ProcessLogger processLogger;

    public void start(Config config) {
        logger.info("### LOAD TEST CONFIGURATION RECEIVED ###");
        String baseURL = config.getBaseURL();
        LinkedHashSet<LoadTest> loadTests = config.getLoadTests();

        for(LoadTest loadTest : loadTests) {
            String script = mapper.getScript(baseURL, loadTest);
            writer.write(script);
            this.runTest();
        }
    }

    private void runTest() {

    }
}