package dq.adapter;

import dq.dqlang.constants.ResponseTime;
import dq.dqlang.k6.request.Checks;
import dq.dqlang.k6.request.Request;
import dq.dqlang.loadtest.Endpoint;
import dq.dqlang.loadtest.Response;
import dq.dqlang.loadtest.ResponseMeasure;
import dq.exception.UnknownResponseTimeTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Map;

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

        String requestDuration = this.getRequestDuration(responseMeasure);
        LinkedHashSet<Integer> statusCodes = this.getStatusCodes(endpoint);
        Checks checks = new Checks(statusCodes, requestDuration);

        Request request = new Request(path, type, pathVariables, params, payload, checks);
        return request;
    }

    private String markPathVariables(String field) {
        //TODO
    }

    private String getRequestDuration(ResponseMeasure responseMeasure) {
        String responseTimeTerm = responseMeasure.getResponseTime();
        ResponseTime responseTime = constantsLoader.load().getResponseTime();
        int duration;
        switch (responseTimeTerm) {
            case "SATISFIED" -> duration = responseTime.getSatisfied();
            case "TOLERATED" -> duration = responseTime.getTolerated();
            case "FRUSTRATED" -> duration = responseTime.getFrustrated();
            default -> throw new UnknownResponseTimeTerm(responseTimeTerm);
        }

        String aggregation = responseTime.getAggregation();
        String requestDuration = aggregation + "<" + duration;
        return requestDuration;
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