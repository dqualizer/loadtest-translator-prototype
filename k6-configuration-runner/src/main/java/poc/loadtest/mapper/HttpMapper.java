package poc.loadtest.mapper;

import org.springframework.stereotype.Component;
import poc.dqlang.request.Request;
import poc.loadtest.exception.UnknownRequestTypeException;

import java.util.Map;
import java.util.Set;

@Component
public class HttpMapper implements k6Mapper {

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
        Set<String> variables = pathVariables.keySet();

        for(String variable : variables) {
            String randomVariable = String.format("%slet %s = %s_array[Math.floor(Math.random() * %s_array.length)];%s",
                    newLine, variable, variable, variable, newLine);
            httpBuilder.append(randomVariable);
        }

        Map<String, String> payload = request.getPayload();
        Map<String, String> params = request.getParams();

        String extraParams = "";
        if(!payload.isEmpty() || !params.isEmpty()) {
            if(!payload.isEmpty() && !params.isEmpty()) {
                httpBuilder.append(this.randomPayloadScript());
                extraParams = ", JSON.stringify(payload), params";
            }
            else if(!payload.isEmpty()) {
                httpBuilder.append(this.randomPayloadScript());
                extraParams = ", JSON.stringify(payload)";
            }
            else {
                extraParams = ", params";
            }
        }
        String httpRequest = String.format("%slet response = http.%s(baseURL + `%s`%s);%s",
                newLine, method, path, extraParams, newLine);
        httpBuilder.append(httpRequest);

        return httpBuilder.toString();
    }

    private String exportFunctionScript() {
        return "export default function() {";
    }

    private String randomPayloadScript() {
        return String.format("%slet payload = payloads[Math.floor(Math.random() * payloads.length)];%s",
                newLine, newLine);
    }
}