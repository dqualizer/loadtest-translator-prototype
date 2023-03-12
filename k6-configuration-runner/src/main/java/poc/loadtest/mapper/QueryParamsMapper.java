package poc.loadtest.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.config.PathConfig;
import poc.dqlang.request.Request;
import poc.loadtest.exception.NoReferenceFoundException;
import poc.util.MyFileReader;

import java.util.Map;
import java.util.Optional;

/**
 * Maps the url-parameter to Javascript-Code
 */
@Component
public class QueryParamsMapper implements k6Mapper {

    @Autowired
    private PathConfig paths;
    @Autowired
    private MyFileReader reader;

    @Override
    public String map(Request request) {
        Map<String, String> queryParams = request.getQueryParams();
        Optional<String> maybeReference = queryParams.values().stream().findFirst();
        if(maybeReference.isEmpty()) throw new NoReferenceFoundException(queryParams);

        String referencePath = paths.getResourcePath() + maybeReference.get();
        String queryParamsObject = reader.readFile(referencePath);

        return """
                const queryParams = %s
                const searchParams = queryParams['params'];
                
                """.formatted(queryParamsObject);
    }
}
