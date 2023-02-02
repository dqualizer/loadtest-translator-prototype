package dq.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.config.PathConfig;
import dq.dqlang.mapping.Mapping;
import dq.exception.InvalidMappingSchemaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;

@Component
public class MappingLoader {

    @Autowired
    private PathConfig paths;

    @EventListener(ApplicationReadyEvent.class)
    public Mapping load() throws IOException {
        String mappingPath = paths.getMapping();
        String mappingJSON = Files.readString(Paths.get(mappingPath));

        ObjectMapper objectMapper = new ObjectMapper();
        Mapping mapping;

        try {
            mapping = objectMapper.readValue(mappingJSON, Mapping.class);
        } catch (Exception e) {
            throw new InvalidMappingSchemaException(e.getMessage());
        }

        return mapping;
    }
}