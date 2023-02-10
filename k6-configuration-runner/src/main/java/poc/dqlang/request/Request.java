package poc.dqlang.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    private String type;
    private String path;
    @JsonProperty("path_variables")
    private Map<String, String> pathVariables;
    private Map<String, String> params;
    private Map<String, String> payload;
    private Checks checks;
}