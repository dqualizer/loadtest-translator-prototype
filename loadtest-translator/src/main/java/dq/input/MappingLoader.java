package dq.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.config.PathConfig;
import dq.dqlang.mapping.Mapping;
import dq.exception.ContextNotFoundException;
import dq.exception.InvalidMappingSchemaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MappingLoader {

    @Autowired
    private PathConfig paths;

    public Mapping load(String context) throws IOException {
        String mappingFolder = paths.getMappingFolder();

        Optional<Mapping> maybeMapping = Files.walk(Paths.get(mappingFolder))
                .filter(Files::isRegularFile)
                .map(this::readFile)
                .map(this::readJSON)
                .filter(mapping -> mapping.getContext().equals(context))
                .findFirst();

        if(maybeMapping.isPresent()) return maybeMapping.get();
        else throw new ContextNotFoundException(context);
    }

    private Mapping readJSON(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, Mapping.class);
        } catch (IOException e) {
            throw new InvalidMappingSchemaException(e.getMessage());
        }
    }

    private String readFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}