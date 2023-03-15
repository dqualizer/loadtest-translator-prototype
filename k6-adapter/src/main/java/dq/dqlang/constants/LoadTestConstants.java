package dq.dqlang.constants;

import com.fasterxml.jackson.annotation.JsonProperty;
import dq.dqlang.constants.accuracy.Accuracy;
import dq.dqlang.constants.loadprofile.LoadProfile;
import lombok.Getter;
import lombok.ToString;

/**
 * Java class for load test constants definitions
 */
@Getter
@ToString
public class LoadTestConstants {

    private Accuracy accuracy;
    @JsonProperty("load_profile")
    private LoadProfile loadProfile;
    @JsonProperty("response_time")
    private ResponseTime responseTime;
}