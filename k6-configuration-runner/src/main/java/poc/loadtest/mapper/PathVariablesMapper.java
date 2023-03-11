package poc.loadtest.mapper;

import org.json.JSONObject;
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
        Optional<String> maybeReference = pathVariables.values().stream().findFirst();
        if(maybeReference.isEmpty()) throw new NoReferenceFoundException(pathVariables);

        String referencePath = paths.getResourcePath() + maybeReference.get();
        String pathVariablesString = reader.readFile(referencePath);
        String pathVariablesScript = String.format("%sconst path_variables = %s",
                newLine, pathVariablesString);
        pathVariablesBuilder.append(pathVariablesScript);

        JSONObject pathVariablesJSON = new JSONObject(pathVariablesString);
        Set<String> variables = pathVariablesJSON.keySet();
        for(String variable : variables) {
            String particularPathVariablesScript = String.format("%sconst %s_array = path_variables['%s'];",
                    newLine, variable, variable);
            pathVariablesBuilder.append(particularPathVariablesScript);
        }

        return pathVariablesBuilder.toString();
    }
}