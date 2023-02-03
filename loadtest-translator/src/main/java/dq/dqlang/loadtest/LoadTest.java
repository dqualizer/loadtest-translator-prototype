package dq.dqlang.loadtest;

import com.fasterxml.jackson.annotation.JsonProperty;
import dq.dqlang.mapping.Endpoint;
import dq.dqlang.modeling.ResponseMeasure;
import dq.dqlang.modeling.Stimulus;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoadTest {

    private Object artifact;
    private String description;
    private Stimulus stimulus;
    @JsonProperty("response_measure")
    private ResponseMeasure responseMeasure;
    private Endpoint endpoint;
}