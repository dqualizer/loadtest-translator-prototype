package dq.dqlang.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Output {

    private Map<String, DataType> contentTypes;
    @JsonProperty("expected_code")
    private String expectedCode;
}