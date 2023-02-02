package dq.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.config.PathConfig;
import dq.dqlang.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class MappingLoader {

    @Autowired
    private PathConfig paths;

    public Mapping load() throws IOException {
        String mappingPath = paths.getMapping();
        String mappingJSON = Files.readString(Paths.get(mappingPath));

        ObjectMapper objectMapper = new ObjectMapper();
        Mapping mapping = objectMapper.readValue(mappingJSON, Mapping.class);
        return mapping;
    }
}