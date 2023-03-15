package dq.dqlang.constants.loadprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoadIncrease {

    @JsonProperty("start_target")
    private int startTarget;
    @JsonProperty("end_target")
    private int endTarget;
    @JsonProperty("stage_duration")
    private String stageDuration;
    private int linear;
    private int quadratic;
    private int cubic;
}