package dq.dqlang.loadtest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashSet;

@Getter
@ToString
public class LoadTestConfig {

    private int version;
    private String context;
    private String environment;
    @JsonProperty("base_url")
    private String baseURL;
    @JsonProperty("load_tests")
    private LinkedHashSet<LoadTest> loadTests;
}