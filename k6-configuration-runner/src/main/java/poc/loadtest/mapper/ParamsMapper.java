package poc.loadtest.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.config.PathConfig;
import poc.dqlang.request.Request;
import poc.loadtest.exception.NoReferenceFoundException;
import poc.util.MyFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Maps the request-parameter to Javascript-Code
 */
@Component
public class ParamsMapper implements k6Mapper {

    @Autowired
    private PathConfig paths;
    @Autowired
    private MyFileReader reader;

    @Override
    public String map(Request request) {
        Map<String, String> params = request.getParams();
        Optional<String> maybeReference = params.values().stream().findFirst();
        if(maybeReference.isEmpty()) return String.format("%sconst params = {}%s", newLine, newLine);

        String referencePath = paths.getResourcePath() + maybeReference.get();
        String paramsObject = reader.readFile(referencePath);

        return String.format("%sconst params = %s%s",
                newLine, paramsObject, newLine);
    }
}