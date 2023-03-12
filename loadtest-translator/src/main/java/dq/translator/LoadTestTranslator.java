package dq.translator;

import dq.dqlang.loadtest.LoadTest;
import dq.dqlang.loadtest.LoadTestConfig;
import dq.dqlang.mapping.*;
import dq.dqlang.modeling.*;
import dq.exception.EnvironmentNotFoundException;
import dq.exception.IDNotFoundException;
import dq.exception.TooManyReferencesException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Translates the modeling with the help of a mapping to a loadtest configuration
 */
@Component
public class LoadTestTranslator {

    public LoadTestConfig translate(Modeling modeling, Mapping mapping) {
        LinkedHashSet<MappingObject> objects = mapping.getObjects();

        int version = modeling.getVersion();
        String context = modeling.getContext();
        String environment = modeling.getEnvironment();
        String baseURL = this.getHost(mapping, environment);

        LinkedHashSet<LoadTest> loadTests = new LinkedHashSet<>();
        LinkedHashSet<ModeledLoadTest> modeledLoadTests = modeling.getRqa().getLoadtests();

        for(ModeledLoadTest modeledLoadTest: modeledLoadTests) {
            String description = modeledLoadTest.getDescription();
            Stimulus stimulus = modeledLoadTest.getStimulus();
            ResponseMeasure responseMeasure = modeledLoadTest.getResponseMeasure();
            Artifact artifact = modeledLoadTest.getArtifact();

            if(artifact.getActivity() == null) {
                List<Endpoint> endpoints = this.getEndpoints(objects, modeledLoadTest);
                for(Endpoint endpoint: endpoints) {
                    LoadTest oneLoadTest = new LoadTest(artifact, description, stimulus, responseMeasure, endpoint);
                    loadTests.add(oneLoadTest);
                }
            }
            else {
                Endpoint endpoint = this.getEndpoint(objects, modeledLoadTest);
                LoadTest oneLoadTest = new LoadTest(artifact, description, stimulus, responseMeasure, endpoint);
                loadTests.add(oneLoadTest);
            }
        }
        LoadTestConfig loadTestConfig =  new LoadTestConfig(version, context, environment, baseURL, loadTests);
        return loadTestConfig;
    }

    /**
     * Get the host-address through the environment, specified in the modeling
     * @param mapping The architecture-mapping
     * @param environment The specified environment in the modeling
     * @return Host-address for the specified environment
     */
    private String getHost(Mapping mapping, String environment) {
        LinkedHashSet<ServerInfo> serverInfos = mapping.getServerInfo();
        Optional<ServerInfo> maybeServerInfo = serverInfos.stream()
                .filter(info -> info.getEnvironment().equals(environment))
                .findFirst();
        if (maybeServerInfo.isPresent()) return maybeServerInfo.get().getHost();
        else throw new EnvironmentNotFoundException(environment);
    }

    /**
     * Get one endpoint for an activity mentioned in the modeled loadtest
     * @param objects A list of all objects inside the mapping
     * @param modeledLoadTest A modeled loadtest inside the modeling
     * @return One endpoint object
     */
    private Endpoint getEndpoint(LinkedHashSet<MappingObject> objects, ModeledLoadTest modeledLoadTest) {
        Artifact artifact = modeledLoadTest.getArtifact();
        Parametrization parametrization = modeledLoadTest.getParametrization();
        String objectID = artifact.getObject();
        String activityID = artifact.getActivity();

        //Find necessary object
        Optional<MappingObject> maybeObject = objects.stream()
                .filter(object -> object.getDqID().equals(objectID))
                .findFirst();
        if(maybeObject.isPresent()) {
            //Find necessary activity
            Optional<Activity> maybeActivity = maybeObject.get()
                    .getActivities().stream()
                    .filter(activity -> activity.getDqID().equals(activityID))
                    .findFirst();
            if(maybeActivity.isPresent()) {
                Endpoint endpoint = maybeActivity.get().getEndpoint();
                Endpoint adoptedEndpoint = this.adoptParametrization(endpoint, parametrization);

                return adoptedEndpoint;
            }
            else throw new IDNotFoundException(activityID);
        }
        else throw new IDNotFoundException(objectID);
    }

    /**
     * Get all Endpoints within one actor/work object mentioned in the modeled loadtest
     * @param objects A list of all objects inside the mapping
     * @param modeledLoadTest A modeled loadtest inside the modeling
     * @return A list with endpoint objects
     */
    private List<Endpoint> getEndpoints(LinkedHashSet<MappingObject> objects, ModeledLoadTest modeledLoadTest) {
        Artifact artifact = modeledLoadTest.getArtifact();
        Parametrization parametrization = modeledLoadTest.getParametrization();
        String objectID = artifact.getObject();

        //Finde necessary object
        Optional<MappingObject> maybeObject = objects.stream()
                .filter(x -> x.getDqID().equals(objectID))
                .findFirst();
        if(maybeObject.isPresent()) {
            //Find all existing endpoints inside this object
            List<Endpoint> endpoints = maybeObject.get()
                    .getActivities().stream()
                    .map(Activity::getEndpoint)
                    .filter(Objects::nonNull).toList();

            List<Endpoint> adoptedEndpoints = endpoints.stream()
                    .map(endpoint -> this.adoptParametrization(endpoint, parametrization)).toList();

            return adoptedEndpoints;
        }
        else throw new IDNotFoundException(objectID);
    }

    /**
     * Compare the parametrization from the modeling and from the mapping
     * The parametrization from the modeling overwrites the parametrization inside an endpoint in the mapping
     * @param endpoint Endpoint inside the mapping
     * @param parametrization Defined parametrization in the modeling
     * @return Endpoint with overwritten parametrization
     */
    private Endpoint adoptParametrization(Endpoint endpoint, Parametrization parametrization) {
        String field = endpoint.getField();
        String operation = endpoint.getOperation();
        LinkedHashSet<Response> responses = endpoint.getResponses();

        Map<String, String> pathVariables = parametrization.getPathVariables();
        Map<String, String> urlParameter = parametrization.getUrlParameter();
        Map<String, String> requestParameter = parametrization.getRequestParameter();
        Map<String, String> payload = parametrization.getPayload();
        if(urlParameter.size() > 1)
            throw new TooManyReferencesException(urlParameter.keySet());
        if(requestParameter.size() > 1)
            throw new TooManyReferencesException(requestParameter.keySet());
        if(payload.size() > 1)
            throw new TooManyReferencesException(payload.keySet());

        Endpoint adoptedEndpoint = new Endpoint(
                field, operation, pathVariables, urlParameter, requestParameter, payload, responses
        );

        return adoptedEndpoint;
    }
}