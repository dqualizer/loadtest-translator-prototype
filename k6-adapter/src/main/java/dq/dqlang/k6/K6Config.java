package dq.dqlang.k6;

import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashSet;

/**
 * Java class for k6 loadtest configurations
 */
@Getter
@ToString
public class K6Config {

    private String name;
    private String baseURL;
    private LinkedHashSet<K6LoadTest> loadTests;
}