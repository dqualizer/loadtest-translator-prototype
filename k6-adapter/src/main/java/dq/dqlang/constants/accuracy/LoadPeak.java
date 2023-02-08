package dq.dqlang.constants.accuracy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoadPeak {

    @JsonProperty("min_repetitions")
    private int minRepetition;
    @JsonProperty("max_repetitions")
    private int maxRepetition;

    private int high;
    @JsonProperty("very_high")
    private int veryHigh;
    @JsonProperty("extremely_high")
    private int extremelyHigh;

    private String slow;
    private String fast;
    @JsonProperty("very_fast")
    private String veryFast;
}