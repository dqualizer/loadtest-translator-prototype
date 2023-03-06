package dq.translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.dqlang.loadtest.LoadTestConfig;
import dq.dqlang.mapping.Mapping;
import dq.dqlang.modeling.Modeling;
import dq.translator.LoadTestTranslator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class LoadTestTranslatorTest {

    private final LoadTestTranslator translator = new LoadTestTranslator();

    private final String loadTestFile = "loadtest-werkstatt.json";
    private final String mappingFile = "mapping-werkstatt.json";
    private final String host = "localhost:9000";
    private final String modelingFile = "modeling-werkstatt.json";

    @Test
    void translationWasSuccessful() throws IOException {
        String loadTestConfigJSON = this.loadLoadTestConfig(loadTestFile);
        String mappingJSON = this.loadMapping(mappingFile);
        String modelingJSON = this.loadModeling(modelingFile);
        ObjectMapper objectMapper = new ObjectMapper();

        LoadTestConfig loadTestConfig = objectMapper.readValue(loadTestConfigJSON, LoadTestConfig.class);
        Mapping mapping = objectMapper.readValue(mappingJSON, Mapping.class);
        Modeling modeling = objectMapper.readValue(modelingJSON, Modeling.class);

        LoadTestConfig translatedLoadTestConfig = translator.translate(modeling, mapping);

        assertThat(loadTestConfig).isEqualToComparingFieldByFieldRecursively(translatedLoadTestConfig);
    }

    private String loadLoadTestConfig(String filePath) throws IOException {
        String resources = this.getResources();
        String absolutePath = resources + filePath;
        return Files.readString(Paths.get(absolutePath));
    }

    private String loadMapping(String filePath) throws IOException {
        String resources = this.getResources();
        String absolutePath = resources + filePath;
        return Files.readString(Paths.get(absolutePath));
    }

    private String loadModeling(String filePath) throws IOException {
        String resources = this.getResources();
        String absolutePath = resources + filePath;
        return Files.readString(Paths.get(absolutePath));
    }

    private String getResources() {
        String resources = new File("src/test/resources").getAbsolutePath();
        return resources + "/translation/";
    }
}