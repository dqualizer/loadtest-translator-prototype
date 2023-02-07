package dq.dqlang.constants;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoadTestConstants {

    private Accuracy accuracy;
    @JsonProperty("response_time")
    private ResponseTime responseTime;
}