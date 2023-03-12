package dq.dqlang.modeling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Java class for the modeling
 */
@Getter
@ToString
public class Modeling {

    private int version;
    private String context;
    private String environment;
    @JsonProperty("runtime_quality_analysis")
    private RuntimeQualityAnalysis rqa;
}