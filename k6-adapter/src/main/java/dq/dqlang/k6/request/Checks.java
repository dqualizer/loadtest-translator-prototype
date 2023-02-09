package dq.dqlang.k6.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Checks {

    @JsonProperty("status_codes")
    private LinkedHashSet<Integer> statusCodes;
    @JsonProperty("duration")
    private int duration;
}