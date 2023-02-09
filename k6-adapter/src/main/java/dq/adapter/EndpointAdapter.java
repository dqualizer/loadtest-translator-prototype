package dq.adapter;

import dq.dqlang.constants.LoadTestConstants;
import dq.dqlang.constants.ResponseTime;
import dq.dqlang.k6.request.Checks;
import dq.dqlang.k6.request.Request;
import dq.dqlang.loadtest.Endpoint;
import dq.dqlang.loadtest.Response;
import dq.dqlang.loadtest.ResponseMeasure;
import dq.exception.UnknownTermException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EndpointAdapter {

    @Autowired
    private ConstantsLoader constantsLoader;

    public Request adaptEndpoint(Endpoint endpoint, ResponseMeasure responseMeasure) {
        String field = endpoint.getField();
        String path = this.markPathVariables(field);
        String type = endpoint.getOperation();
        Map<String, String> pathVariables = endpoint.getPathVariables();
        Map<String, String> params = endpoint.getParameter();
        Map<String, String> payload = endpoint.getPayload();

        int duration = this.getDuration(responseMeasure);
        LinkedHashSet<Integer> statusCodes = this.getStatusCodes(endpoint);
        Checks checks = new Checks(statusCodes, duration);

        return new Request(path, type, pathVariables, params, payload, checks);
    }

    /**
     * At first the method finds all path variables inside the field with the help of a regex pattern.
     * The pattern is looking for text that is enclosed by curly brackets {}.
     * Those variables are saved inside a list, including the brackets. Duplicates will be combined to one variable.
     * After that a "$"-symbol will be added to all found variables inside the field
     *
     * @param field Path with unmarked variables
     * @return Path with marked variables, for example "{id}" turns into "${id}"
     */
    private String markPathVariables(String field) {
        Pattern pattern = Pattern.compile("\\{.*?}");
        Matcher matcher = pattern.matcher(field);
        List<String> variables = new LinkedList<>();

        while (matcher.find()) {
            String foundVariable = matcher.group();
            variables.add(foundVariable);
        }

        String markedPathVariables = variables.stream()
                .distinct()
                .reduce(field, (path, variable) -> path.replace(variable, "$"+ variable));
        return markedPathVariables;
    }

    private int getDuration(ResponseMeasure responseMeasure) {
        LoadTestConstants constants = constantsLoader.load();
        ResponseTime responseTime = constants.getResponseTime();

        String responseTimeTerm = responseMeasure.getResponseTime();
        int duration;
        switch (responseTimeTerm) {
            case "SATISFIED" -> duration = responseTime.getSatisfied();
            case "TOLERATED" -> duration = responseTime.getTolerated();
            case "FRUSTRATED" -> duration = responseTime.getFrustrated();
            default -> throw new UnknownTermException(responseTimeTerm);
        }
        return duration;
    }

    private LinkedHashSet<Integer> getStatusCodes(Endpoint endpoint) {
        LinkedHashSet<Response> responses = endpoint.getResponses();
        LinkedHashSet<Integer> statusCodes = new LinkedHashSet<>();

        for(Response response : responses) {
            int statusCode = response.getExpectedCode();
            statusCodes.add(statusCode);
        }
        return statusCodes;
    }
}