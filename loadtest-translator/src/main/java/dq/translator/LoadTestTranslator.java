package dq.translator;

import dq.dqlang.loadtest.LoadTest;
import dq.dqlang.loadtest.LoadTestConfig;
import dq.dqlang.mapping.*;
import dq.dqlang.modeling.*;
import dq.exception.EnvironmentNotFoundException;
import dq.exception.IDNotFoundException;
import dq.mock.URLRetrieverMock;
import dq.util.ConstantReplacer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoadTestTranslator {

    @Autowired
    private URLRetrieverMock urlRetriever;

    public LoadTestConfig translate(Modeling modeling, Mapping mapping) {
        LinkedHashSet<MappingObject> objects = mapping.getObjects();

        int version = modeling.getVersion();
        String context = modeling.getContext();
        String environment = modeling.getEnvironment();
        String host = this.getHost(mapping, environment);
        String baseURL = this.getURL(host);

        LinkedHashSet<LoadTest> loadTests = new LinkedHashSet<>();
        LinkedHashSet<ModeledLoadTest> modeledLoadTests = modeling.getRqa().getLoadtests();
        for(ModeledLoadTest modeledLoadTest: modeledLoadTests) {

            String description = modeledLoadTest.getDescription();
            Stimulus stimulus = modeledLoadTest.getStimulus();
            ResponseMeasure responseMeasure = modeledLoadTest.getResponseMeasure();
            Artifact artifact = modeledLoadTest.getArtifact();

            if(artifact.getActivity() == null) {
                List<Endpoint> endpoints = this.getEndpoints(objects, artifact);
                for(Endpoint endpoint: endpoints) {
                    LoadTest oneLoadTest = new LoadTest(artifact, description, stimulus, responseMeasure, endpoint);
                    loadTests.add(oneLoadTest);
                }
            }
            else {
                Endpoint endpoint = this.getEndpoint(objects, artifact);
                LoadTest oneLoadTest = new LoadTest(artifact, description, stimulus, responseMeasure, endpoint);
                loadTests.add(oneLoadTest);
            }
        }
        LoadTestConfig loadTestConfig =  new LoadTestConfig(version, context, environment, baseURL, loadTests);
        return loadTestConfig;
    }

    private String getHost(Mapping mapping, String environment) {
        LinkedHashSet<ServerInfo> serverInfos = mapping.getServerInfo();
        Optional<ServerInfo> maybeServerInfo = serverInfos.stream()
                .filter(info -> info.getEnvironment().equals(environment))
                .findFirst();
        if (maybeServerInfo.isPresent()) return maybeServerInfo.get().getHost();
        else throw new EnvironmentNotFoundException(environment);
    }

    private String getURL(String url) {
        return urlRetriever.retrieve(url);
    }

    private Endpoint getEndpoint(LinkedHashSet<MappingObject> objects, Artifact artifact) {
        String objectID = artifact.getObject();
        String activityID = artifact.getActivity();

        Optional<MappingObject> maybeObject = objects.stream()
                .filter(x -> x.getDqID().equals(objectID))
                .findFirst();
        if(maybeObject.isPresent()) {
            Optional<Activity> maybeActivity = maybeObject.get()
                    .getActivities().stream()
                    .filter(x -> x.getDqID().equals(activityID))
                    .findFirst();
            if(maybeActivity.isPresent()) {
                return maybeActivity.get().getEndpoint();
            }
            else throw new IDNotFoundException(activityID);
        }
        else throw new IDNotFoundException(objectID);
    }

    private List<Endpoint> getEndpoints(LinkedHashSet<MappingObject> objects, Artifact artifact) {
        String objectID = artifact.getObject();

        Optional<MappingObject> maybeObject = objects.stream()
                .filter(x -> x.getDqID().equals(objectID))
                .findFirst();
        if(maybeObject.isPresent()) {
            List<Endpoint> endpoints = maybeObject.get()
                    .getActivities().stream()
                    .map(Activity::getEndpoint)
                    .filter(Objects::nonNull).toList();
            return endpoints;
        }
        else throw new IDNotFoundException(objectID);
    }
}