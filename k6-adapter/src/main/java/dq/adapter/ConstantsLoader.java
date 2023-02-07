package dq.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dq.config.PathConfig;
import dq.dqlang.constants.LoadTestConstants;
import dq.exception.InvalidConstantsSchemaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ConstantsLoader {

    @Autowired
    private PathConfig paths;

    public LoadTestConstants load() {
        String constantsPath = paths.getConstants();

        String constantsString = "";
        try {
            constantsString = Files.readString(Paths.get(constantsPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        LoadTestConstants loadTestConstants;

        try {
            loadTestConstants = objectMapper.readValue(constantsString, LoadTestConstants.class);
        } catch (Exception e) {
            throw new InvalidConstantsSchemaException(e.getMessage());
        }

        return loadTestConstants;
    }
}