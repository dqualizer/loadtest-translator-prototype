package dq.dqlang.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Response {

    @JsonProperty("expected_code")
    private int expectedCode;
    private String type;
}