package dq.adapter;

import dq.dqlang.constants.Accuracy;
import dq.dqlang.constants.LoadTestConstants;
import dq.dqlang.constants.ResponseTime;
import dq.dqlang.k6.K6Config;
import dq.dqlang.k6.K6LoadTest;
import dq.dqlang.k6.options.Options;
import dq.dqlang.k6.request.Checks;
import dq.dqlang.k6.request.Request;
import dq.dqlang.loadtest.*;
import dq.exception.UnknownResponseTimeTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Map;

@Component
public class K6Adapter {

    @Autowired
    private ConstantsLoader constantsLoader;
    @Autowired
    private EndpointAdapter endpointAdapter;

    public K6Config adapt(LoadTestConfig loadTestConfig) {
        String name = loadTestConfig.getContext();
        String baseURL = loadTestConfig.getBaseURL();
        LinkedHashSet<LoadTest> loadTests = loadTestConfig.getLoadTests();
        LinkedHashSet<K6LoadTest> k6LoadTests = new LinkedHashSet<>();

        for(LoadTest loadTest : loadTests) {
            Stimulus stimulus = loadTest.getStimulus();
            Endpoint endpoint = loadTest.getEndpoint();
            ResponseMeasure responseMeasure = loadTest.getResponseMeasure();
            String loadProfile = stimulus.getLoadProfile();
            int repetition = 1;
            if(loadProfile.equals("LOAD_PEAK"))
                repetition = this.calculateRepetition(stimulus.getAccuracy());

            Options options = this.adaptStimulus(stimulus);
            Request request = endpointAdapter.adaptEndpoint(endpoint, responseMeasure);

            K6LoadTest k6LoadTest = new K6LoadTest(repetition, options, request);
            k6LoadTests.add(k6LoadTest);
        }

        K6Config k6Config = new K6Config(name, baseURL, k6LoadTests);
        return k6Config;
    }

    private Options adaptStimulus(Stimulus stimulus) {
        //TODO
    }

    private int calculateRepetition(int accuracy) {
        LoadTestConstants constants = constantsLoader.load();
        Accuracy constantsAccuracy = constants.getAccuracy();
        int max = constantsAccuracy.getMax();
        int min = constantsAccuracy.getMin();

        int repetition = max * (accuracy/100);
        return Math.max(repetition, min);
    }
}