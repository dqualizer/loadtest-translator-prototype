package dq.dqlang.k6;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;

/**
 * Java class for k6 loadtest configurations
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class K6Config {

    private String name;
    private String baseURL;
    private LinkedHashSet<K6LoadTest> loadTests;
}