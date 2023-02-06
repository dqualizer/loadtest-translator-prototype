package dq.dqlang;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.dqlang.mapping.Mapping;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MappingTest {

    private final String file = "mapping.json";

    @Test
    void objectMapperDoesNotThrowException() throws IOException {
        String mappingJSON = this.loadMapping(file);
        ObjectMapper objectMapper = new ObjectMapper();

        assertDoesNotThrow(() -> objectMapper.readValue(mappingJSON, Mapping.class));
    }

    private String loadMapping(String filePath) throws IOException {
        String resources = this.getResources();
        String absolutePath = resources + filePath;
        return Files.readString(Paths.get(absolutePath));
    }

    private String getResources() {
        String resources = new File("src/test/resources").getAbsolutePath();
        return resources + "/dqlang/";
    }
}