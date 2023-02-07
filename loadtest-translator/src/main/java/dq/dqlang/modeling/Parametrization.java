package dq.dqlang.modeling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class Parametrization {

    @JsonProperty("path_variables")
    private Map<String, String> pathVariables;
    private Map<String, String> parameter;
    private Map<String, String> payload;
}