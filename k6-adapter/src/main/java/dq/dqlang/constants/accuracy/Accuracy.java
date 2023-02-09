package dq.dqlang.constants.accuracy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Accuracy {

    private Repetition repetition;
    @JsonProperty("load_peak")
    private LoadPeak loadPeak;
    @JsonProperty("load_increase")
    private LoadIncrease loadIncrease;
    @JsonProperty("constant_load")
    private ConstantLoad constantLoad;
}