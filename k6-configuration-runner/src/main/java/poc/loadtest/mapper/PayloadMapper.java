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

@Component
public class PayloadMapper implements k6Mapper {

    @Autowired
    private PathConfig paths;
    @Autowired
    private MyFileReader reader;

    @Override
    public String map(Request request) {
        Map<String, String> payload = request.getPayload();
        Optional<String> maybeReference = payload.values().stream().findFirst();
        if(maybeReference.isEmpty()) throw new NoReferenceFoundException(payload);

        String referencePath = paths.getResourcePath() + maybeReference.get();
        String payloadObject = reader.readFile(referencePath);

        return """
                const payloadData = %s
                const payloads = payloadData['payloads'];
                
                """.formatted(payloadObject);
    }
}