package dq.dqlang.constants.accuracy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ConstantLoad {

    private int low;
    private int medium;
    private int high;
    @JsonProperty("min_duration")
    @JsonPropertyDescription("min duration in seconds")
    private int minDuration;
    @JsonProperty("max_duration")
    @JsonPropertyDescription("max duration in seconds")
    private int maxDuration;
}