package dq.dqlang.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FieldItem {

    private String path;
    @JsonProperty("operation_id")
    private String operationID;
    private String operation;
    private LinkedHashSet<Input> input;
    private Map<String, DataType> body;
    private LinkedHashSet<Map<String, Output>> output;
}