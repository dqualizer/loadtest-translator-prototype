package dq.dqlang.modeling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashSet;

@Getter
@ToString
public class ModeledLoadTest {

    private Artifact artifact;
    private String description;
    private Stimulus stimulus;
    private Parametrization parametrization;
    @JsonProperty("response_measure")
    private ResponseMeasure responseMeasure;
    @JsonProperty("result_metrics")
    private LinkedHashSet<String> resultMetrics;
}