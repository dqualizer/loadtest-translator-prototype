package dq.dqlang.k6.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class Request {

    private String type;
    private String path;
    @JsonProperty("path_variables")
    private Map<String, String> pathVariables;
    private Map<String, String> params;
    private Map<String, String> payload;
    private Checks checks;
}