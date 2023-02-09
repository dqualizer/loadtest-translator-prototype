package poc.loadtest.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poc.config.PathConfig;
import poc.dqlang.request.Request;
import poc.loadtest.exception.NoReferenceFoundException;
import poc.util.MyFileReader;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class PathVariablesMapper implements k6Mapper {

    @Autowired
    private PathConfig paths;
    @Autowired
    private MyFileReader reader;

    @Override
    public String map(Request request) {
        StringBuilder pathVariablesBuilder = new StringBuilder();
        Map<String, String> pathVariables = request.getPathVariables();
        Set<String> variables = pathVariables.keySet();

        for(String variable : variables) {
            String referencePath = paths.getResourcePath() + pathVariables.get(variable);
            String pathVariablesObject = reader.readFile(referencePath);
            String pathVariablesScript =
                    """
                    const %s_data = %s
                    const %s_array = %s_data['path_variables'];
                    
                    """.formatted(variable, pathVariablesObject, variable, variable);
            pathVariablesBuilder.append(pathVariablesScript);
        }

        return pathVariablesBuilder.toString();
    }
}