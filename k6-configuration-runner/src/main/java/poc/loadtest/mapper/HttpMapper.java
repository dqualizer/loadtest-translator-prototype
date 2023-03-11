package poc.loadtest.mapper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.config.PathConfig;
import poc.dqlang.request.Request;
import poc.loadtest.exception.NoReferenceFoundException;
import poc.loadtest.exception.UnknownRequestTypeException;
import poc.util.MyFileReader;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class HttpMapper implements k6Mapper {

    @Autowired
    private PathConfig paths;
    @Autowired
    private MyFileReader reader;

    @Override
    public String map(Request request) {
        StringBuilder httpBuilder = new StringBuilder();
        httpBuilder.append(this.exportFunctionScript());
        String path = request.getPath();
        String type = request.getType().toUpperCase();
        String method = switch (type) {
            case "GET" -> "get";
            case "POST" -> "post";
            case "PUT" -> "put";
            case "DELETE" -> "del";
            default -> throw new UnknownRequestTypeException(type);
        };

        Map<String, String> pathVariables = request.getPathVariables();
        Optional<String> maybeReference = pathVariables.values().stream().findFirst();
        if(maybeReference.isPresent()) {
            String referencePath = paths.getResourcePath() + maybeReference.get();
            String pathVariablesString = reader.readFile(referencePath);
            JSONObject pathVariablesJSON = new JSONObject(pathVariablesString);

            Set<String> variables = pathVariablesJSON.keySet();
            for(String variable : variables) {
                String randomPathVariable = String.format("%slet %s = %s_array[Math.floor(Math.random() * %s_array.length)];%s",
                    newLine, variable, variable, variable, newLine);
                httpBuilder.append(randomPathVariable);
            }
        }

        Map<String, String> payload = request.getPayload();
        Map<String, String> queryParams = request.getQueryParams();

        String extraParams = "";
        if(!payload.isEmpty() || !queryParams.isEmpty()) {
            if(!payload.isEmpty()  && !queryParams.isEmpty()) {
                httpBuilder.append(this.randomQueryParamScript());
                httpBuilder.append(this.randomPayloadScript());
                extraParams = " + `?${urlSearchParams.toString()}`, JSON.stringify(payload)";
            }
            else if(!payload.isEmpty()) {
                httpBuilder.append(this.randomPayloadScript());
                extraParams = ", JSON.stringify(payload)";
            }
            else {
                httpBuilder.append(this.randomQueryParamScript());
                extraParams = " + `?${urlSearchParams.toString()}`";
            }
        }
        String httpRequest = String.format("%slet response = http.%s(baseURL + `%s`%s, params);%s",
                newLine, method, path, extraParams, newLine);
        httpBuilder.append(httpRequest);

        return httpBuilder.toString();
    }

    private String exportFunctionScript() {
        return String.format("%sexport default function() {%s", newLine, newLine);
    }

    private String randomPayloadScript() {
        return String.format("%slet payload = payloads[Math.floor(Math.random() * payloads.length)];%s",
                newLine, newLine);
    }

    private String randomQueryParamScript() {
        return """
                let currentSearchParams = searchParams[Math.floor(Math.random() * searchParams.length)];
                let urlSearchParams = new URLSearchParams(currentSearchParams);
                """;
    }
}