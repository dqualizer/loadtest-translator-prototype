package dq.dqlang.k6.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonTypeName("constant-vus")
public class ConstantScenario extends Scenario {

    private final String executor = "constant-vus";
    private int vus;
    private String duration;
}