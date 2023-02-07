package dq.dqlang.k6.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashSet;

@Getter
@ToString
@JsonTypeName("ramping-vus")
public class RampingScenario extends Scenario {

    private String executor = "ramping-vus";
    private int startVUs;
    private LinkedHashSet<Stage> stages;
}