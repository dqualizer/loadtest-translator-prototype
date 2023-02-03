package dq.dqlang;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.dqlang.loadtest.LoadTestConfig;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LoadTestConfigTest {

    private final String file = "loadtestConfig.json";

    @Test
    void objectMapperDoesNotThrowException() throws IOException {
        String configJSON = this.loadLoadTestConfig(file);
        ObjectMapper objectMapper = new ObjectMapper();

        assertDoesNotThrow(() -> objectMapper.readValue(configJSON, LoadTestConfig.class));
    }

    private String loadLoadTestConfig(String filePath) throws IOException {
        String resources = this.getResources();
        String absolutePath = resources + filePath;
        return Files.readString(Paths.get(absolutePath));
    }

    private String getResources() {
        String resources = new File("src/test/resources").getAbsolutePath();
        return resources + "/dqlang/";
    }
}