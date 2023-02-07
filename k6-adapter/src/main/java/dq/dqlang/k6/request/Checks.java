package dq.dqlang.k6.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashSet;

@Getter
@ToString
public class Checks {

    private LinkedHashSet<Integer> status;
    @JsonProperty("http_req_duration")
    private String requestDuration;
}