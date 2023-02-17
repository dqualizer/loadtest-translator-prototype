package dq.dqlang.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Map;

@Getter
@Setter
@ToString
public class Endpoint {

    private String field;
    private String operation;
    @JsonProperty("path_variables")
    private Map<String, String> pathVariables;
    @JsonProperty("url_parameter")
    private Map<String, String> urlParameter;
    @JsonProperty("request_parameter")
    private Map<String, String> requestParameter;
    private Map<String, String> payload;
    private LinkedHashSet<Response> responses;
}