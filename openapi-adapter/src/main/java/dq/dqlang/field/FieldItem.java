package dq.dqlang.field;

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
public class FieldItem {

    @JsonProperty("operation_id")
    private String operationID;
    private String operation;
    private LinkedHashSet<Input> input;
    private Body body;
    private LinkedHashSet<Output> output;
}