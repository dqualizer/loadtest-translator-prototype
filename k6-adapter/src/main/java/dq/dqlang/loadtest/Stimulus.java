package dq.dqlang.loadtest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Stimulus {

    @JsonProperty("load_profile")
    private String loadProfile;
    @JsonProperty("highest_load")
    private String highestLoad;
    @JsonProperty("time_to_highest_load")
    private String timeToHighestLoad;
    @JsonProperty("type_of_increase")
    private String typeOfIncrease;
    @JsonProperty("base_load")
    private String baseLoad;
    private int accuracy;
}