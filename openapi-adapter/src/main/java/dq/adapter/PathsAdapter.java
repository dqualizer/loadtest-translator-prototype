package dq.adapter;

import dq.dqlang.field.*;
import io.swagger.v3.core.util.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
public class PathsAdapter {

    public Field getField(JSONObject pathsObject) {
        LinkedHashSet<FieldItem> fieldItems = new LinkedHashSet<>();
        Set<String> paths = pathsObject.keySet();

        for (String path : paths) {
            JSONObject pathObject = pathsObject.getJSONObject(path);
            Set<String> operations = pathObject.keySet();

            for (String operation : operations) {
                JSONObject operationObject = pathObject.getJSONObject(operation);

                String operationID = operationObject.getString("operationId");
                LinkedHashSet<Input> inputs = this.getInput(operationObject);
                Body body = this.getBody(operationObject);
                LinkedHashSet<Output> outputs = this.getOutput(operationObject);

                FieldItem item = new FieldItem(path, operationID, operation, inputs, body, outputs);
                fieldItems.add(item);
            }
        }
        return new Field(fieldItems);
    }

    private LinkedHashSet<Input> getInput(JSONObject operationObject) {
        LinkedHashSet<Input> inputs = new LinkedHashSet<>();
        if (!operationObject.has("parameters")) return inputs;

        JSONArray parameters = operationObject.getJSONArray("parameters");
        for (int i = 0; i < parameters.length(); i++) {
            JSONObject parameter = parameters.getJSONObject(i);
            String name = parameter.getString("name");
            String in = parameter.getString("in");
            boolean required = parameter.getBoolean("required");
            String type = parameter.getJSONObject("schema").getString("type");
            Input input = new Input(name, in, required, type);
            inputs.add(input);
        }
        return inputs;
    }

    private Body getBody(JSONObject operationObject) {
        if (!operationObject.has("requestBody")) return new Body();

        JSONObject requestBody = operationObject.getJSONObject("requestBody");
        JSONObject content = requestBody.getJSONObject("content");
        Map<String, DataType> dataTypes = this.getDataTypes(content);
        return new Body(dataTypes);
    }

    private LinkedHashSet<Output> getOutput(JSONObject operationObject) {
        LinkedHashSet<Output> outputs = new LinkedHashSet<>();
        if (!operationObject.has("responses")) return outputs;

        JSONObject responsesObject = operationObject.getJSONObject("responses");
        Set<String> responses = responsesObject.keySet();

        for (String response : responses) {
            JSONObject oneResponse = responsesObject.getJSONObject(response);
            JSONObject content = oneResponse.getJSONObject("content");
            Map<String, DataType> dataTypes = this.getDataTypes(content);

            Output output = new Output(dataTypes, response);
            outputs.add(output);
        }
        return outputs;
    }

    private Map<String, DataType> getDataTypes(JSONObject contentsObject) {
        Set<String> contents = contentsObject.keySet();
        Map<String, DataType> dataTypes = new LinkedHashMap<>();

        for (String contentType : contents) {
            JSONObject contentObject = contentsObject.getJSONObject(contentType);
            JSONObject schema = contentObject.getJSONObject("schema");

            String type = "";
            if (schema.has("$ref")) {
                String bodyTypeReference = schema.getString("$ref");
                //leave only the name of the data type
                type = bodyTypeReference.replaceAll("(.*)(?<=./)", "");
            } else if (schema.has("type")) type = schema.getString("type");

            DataType dataType = new DataType(type);
            dataTypes.put(contentType, dataType);
        }
        return dataTypes;
    }
}