package poc.loadtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.config.PathConfig;
import poc.dqlang.Config;
import poc.dqlang.LoadTest;
import poc.loadtest.exception.RunnerFailedException;
import poc.loadtest.mapper.ScriptMapper;
import poc.util.HostRetriever;
import poc.util.ProcessLogger;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ConfigRunner {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private PathConfig paths;
    @Autowired
    private HostRetriever hostRetriever;
    @Autowired
    private ScriptMapper mapper;
    @Autowired
    private ScriptWriter writer;
    @Autowired
    private ProcessLogger processLogger;

    public void start(Config config) {
        logger.info("### LOAD TEST CONFIGURATION RECEIVED ###");
        try {
            this.run(config);
        } catch (Exception e) {
            logger.severe("### LOAD TEST FAILED ###");
            e.printStackTrace();
            throw new RunnerFailedException(e.getMessage());
        }
    }

    private void run(Config config) throws IOException, InterruptedException {
        String localBaseURL = config.getBaseURL();
        //If config-runner runs inside docker, localhost can´t be used
        String baseURL = localBaseURL.replace("127.0.0.1", hostRetriever.getHost());

        LinkedHashSet<LoadTest> loadTests = config.getLoadTests();
        int testCounter = 0;

        for(LoadTest loadTest : loadTests) {
            List<String> script = mapper.getScript(baseURL, loadTest);
            String scriptPath = paths.getScript(testCounter);
            writer.write(script, scriptPath);

            int repetition = loadTest.getRepetition();
            int runCounter = 0;

            while (runCounter < repetition) {
                this.runTest(scriptPath, testCounter, runCounter);
                runCounter++;
            }
            testCounter++;
        }
    }

    private void runTest(String scriptPath, int testCounter, int runCounter) throws IOException, InterruptedException {
        String command = ".\\k6.exe run " + scriptPath;
        Process process = Runtime.getRuntime().exec(command);

        String loggingPath = paths.getLogging(testCounter, runCounter);
        processLogger.log(process, loggingPath);
    }
}