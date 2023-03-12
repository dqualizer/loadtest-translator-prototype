package dq.adapter;

import dq.dqlang.constants.LoadTestConstants;
import dq.dqlang.constants.accuracy.Repetition;
import dq.dqlang.k6.K6Config;
import dq.dqlang.k6.K6LoadTest;
import dq.dqlang.k6.options.Options;
import dq.dqlang.k6.request.Request;
import dq.dqlang.loadtest.*;
import dq.input.ConstantsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;

/**
 * Adapts a loadtest configuration to an inoffical k6-configuration
 */
@Component
public class K6Adapter {

    @Autowired
    private ConstantsLoader constantsLoader;
    @Autowired
    private EndpointAdapter endpointAdapter;
    @Autowired
    private StimulusAdapter stimulusAdapter;

    /**
     * Adapt the loadtest configuration. It consists of 3 steps:
     * 1. Adapt global test configuration
     * 2. Adapt the stimulus for every loadtest
     * 3. Adapt the endpoints for every loadtest
     * @param loadTestConfig The received dqlang load test configuration
     * @return An adapted loadtest configuration for k6
     */

    public K6Config adapt(LoadTestConfig loadTestConfig) {
        String name = loadTestConfig.getContext();
        String baseURL = loadTestConfig.getBaseURL();
        LinkedHashSet<LoadTest> loadTests = loadTestConfig.getLoadTests();
        LinkedHashSet<K6LoadTest> k6LoadTests = new LinkedHashSet<>();

        for(LoadTest loadTest : loadTests) {
            Stimulus stimulus = loadTest.getStimulus();
            int repetition = this.calculateRepetition(stimulus);
            Options options = stimulusAdapter.adaptStimulus(stimulus);

            Endpoint endpoint = loadTest.getEndpoint();
            ResponseMeasure responseMeasure = loadTest.getResponseMeasure();
            Request request = endpointAdapter.adaptEndpoint(endpoint, responseMeasure);

            K6LoadTest k6LoadTest = new K6LoadTest(repetition, options, request);
            k6LoadTests.add(k6LoadTest);
        }

        K6Config k6Config = new K6Config(name, baseURL, k6LoadTests);
        return k6Config;
    }

    /**
     * Calculate how many times a loadtest should be repeated based on the defined accuracy
     * @param stimulus Stimulus for one loadtest
     * @return The amount of repetitions
     */
    private int calculateRepetition(Stimulus stimulus) {
        String loadProfile = stimulus.getLoadProfile();
        if(loadProfile.equals("CONSTANT_LOAD")) return 1;

        LoadTestConstants constants = constantsLoader.load();
        Repetition repetitionConstants = constants.getAccuracy().getRepetition();

        int accuracy = stimulus.getAccuracy();
        int max = repetitionConstants.getMax();
        int min = repetitionConstants.getMin();

        int repetition = (int)(max * (accuracy/100.0));
        return Math.max(repetition, min);
    }
}