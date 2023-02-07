package dq.dqlang.k6.options;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Stage {

    private String duration;
    private int target;
}