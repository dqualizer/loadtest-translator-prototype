package dq.dqlang.modeling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Modeling {

    private int version;
    private String context;
    @JsonProperty("runtime_quality_analysis")
    private RuntimeQualityAnalysis rqa;
}