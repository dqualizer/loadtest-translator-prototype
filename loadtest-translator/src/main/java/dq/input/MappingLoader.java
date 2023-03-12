package dq.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.config.PathConfig;
import dq.dqlang.mapping.Mapping;
import dq.exception.ContextNotFoundException;
import dq.exception.InvalidMappingSchemaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Imports all available mapping-files and searches the mapping for the current Bounded Context
 */
@Component
public class MappingLoader {

    @Autowired
    private PathConfig paths;

    /**
     * Find a mapping with the help of a Bounded Context
     * @param context Bounded Context from the modeling
     * @return Mapping for the needed Bounded Context
     * @throws IOException
     * @throws ContextNotFoundException If no Mapping with needed Bounded Context was found
     */
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

    /**
     * Transform a JSON-String to a Mapping java object
     * @param json Mapping as JSON-String
     * @return Mapping as Java object
     */
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