package dq.dqlang;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.dqlang.k6.K6Config;
import dq.dqlang.k6.options.ConstantScenario;
import dq.dqlang.k6.options.Scenario;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class K6ConfigTest {

    private final String file = "k6Config.json";

    @Test
    void objectMapperDoesNotThrowException() throws IOException {
        String configJSON = this.loadK6Config(file);
        ObjectMapper objectMapper = new ObjectMapper();

        assertDoesNotThrow(() -> objectMapper.readValue(configJSON, K6Config.class));
    }

    private String loadK6Config(String filePath) throws IOException {
        String resources = this.getResources();
        String absolutePath = resources + filePath;
        return Files.readString(Paths.get(absolutePath));
    }

    private String getResources() {
        String resources = new File("src/test/resources").getAbsolutePath();
        return resources + "/dqlang/";
    }
}