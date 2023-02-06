package dq.dqlang;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.dqlang.modeling.Modeling;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ModelingTest {

    private final String file = "modeling.json";

    @Test
    void objectMapperDoesNotThrowException() throws IOException {
        String modelingJSON = this.loadModeling(file);
        ObjectMapper objectMapper = new ObjectMapper();

        assertDoesNotThrow(() -> objectMapper.readValue(modelingJSON, Modeling.class));
    }

    private String loadModeling(String filePath) throws IOException {
        String resources = this.getResources();
        String absolutePath = resources + filePath;
        return Files.readString(Paths.get(absolutePath));
    }

    private String getResources() {
        String resources = new File("src/test/resources").getAbsolutePath();
        return resources + "/dqlang/";
    }
}