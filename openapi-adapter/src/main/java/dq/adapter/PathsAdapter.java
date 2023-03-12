package dq.adapter;

import dq.dqlang.field.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Adapts the paths from an OpenAPI-schema
 */
@Component
public class PathsAdapter {

    /**
     * Adapt every path inside the OpenAPI-schema to a field inside the dqlang-API-schema
     * @param pathsObject A JSONObject with all paths inside
     * @return A set of fields for the dqlang-API-schema
     */
    public LinkedHashSet<FieldItem> getField(JSONObject pathsObject) {
        LinkedHashSet<FieldItem> fieldItems = new LinkedHashSet<>();
        Set<String> paths = pathsObject.keySet();

        //iterate through all existing paths
        for (String path : paths) {
            JSONObject pathObject = pathsObject.getJSONObject(path);
            Set<String> operations = pathObject.keySet();

            //iterate through all existing operations for one path
            for (String operation : operations) {
                JSONObject operationObject = pathObject.getJSONObject(operation);

                String operationID = operationObject.getString("operationId");
                LinkedHashSet<Input> inputs = this.getInput(operationObject);
                Map<String, DataType> body = this.getBody(operationObject);
                LinkedHashSet<Map<String, Output>> outputs = this.getOutput(operationObject);

                FieldItem item = new FieldItem(path, operationID, operation, inputs, body, outputs);
                fieldItems.add(item);
            }
        }
        return fieldItems;
    }

    /**
     * Adapt the inputs for one path-operation
     * @param operationObject The operation-JSONObject inside one path
     * @return An adapted set of inputs for one path-operation
     */
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

    /**
     * Adapt the bodies for one path-operation
     * @param operationObject The operation-JSONObject inside one path
     * @return An adapted body-JSONObject for one path-operation
     */
    private Map<String, DataType> getBody(JSONObject operationObject) {
        if (!operationObject.has("requestBody")) return new LinkedHashMap<>();

        JSONObject requestBody = operationObject.getJSONObject("requestBody");
        JSONObject content = requestBody.getJSONObject("content");
        Map<String, DataType> body = this.getDataTypes(content);
        return body;
    }

    private Map<String, DataType> getDataTypes(JSONObject contentsObject) {
        Set<String> contents = contentsObject.keySet();
        Map<String, DataType> dataTypes = new LinkedHashMap<>();

        for (String contentType : contents) {
            JSONObject contentObject = contentsObject.getJSONObject(contentType);
            JSONObject schema = contentObject.getJSONObject("schema");

            String type = "";
            if (schema.has("$ref")) {
                //example for a type reference: "#/components/schemas/Book"
                String bodyTypeReference = schema.getString("$ref");
                //leave only the name of the data type and remove the reference-path
                type = bodyTypeReference.replaceAll("(.*)(?<=./)", "");
            } else if (schema.has("type")) type = schema.getString("type");

            DataType dataType = new DataType(type);
            dataTypes.put(contentType, dataType);
        }
        return dataTypes;
    }

    /**
     * Adapt the outputs for one path-operation
     * @param operationObject The operation-JSONObject inside one path
     * @return An adapted set of outputs for the path-operation
     */
    private LinkedHashSet<Map<String, Output>> getOutput(JSONObject operationObject) {
        LinkedHashSet<Map<String, Output>> outputs = new LinkedHashSet<>();
        if (!operationObject.has("responses")) return outputs;

        JSONObject responsesObject = operationObject.getJSONObject("responses");
        Set<String> responses = responsesObject.keySet();

        for (String response : responses) {
            JSONObject oneResponse = responsesObject.getJSONObject(response);
            JSONObject content = oneResponse.getJSONObject("content");

            Map<String, Output> output = this.getOneOutput(content, response);
            outputs.add(output);
        }
        return outputs;
    }

    /**
     * Adapt one particular output
     * @param contentsObject The content-type-object within one output-object
     * @param expectedCode The expected status code for this output
     * @return A map with particular content-types as keys and Output-objects as values
     */
    private Map<String, Output> getOneOutput(JSONObject contentsObject, String expectedCode) {
        Set<String> contents = contentsObject.keySet();
        Map<String, Output> output = new LinkedHashMap<>();

        for (String contentType : contents) {
            JSONObject contentObject = contentsObject.getJSONObject(contentType);
            JSONObject schema = contentObject.getJSONObject("schema");

            String type = "";
            if (schema.has("$ref")) {
                String bodyTypeReference = schema.getString("$ref");
                //leave only the name of the data type
                type = bodyTypeReference.replaceAll("(.*)(?<=./)", "");
            } else if (schema.has("type")) type = schema.getString("type");

            Output oneOutput = new Output(type, expectedCode);
            output.put(contentType, oneOutput);
        }
        return output;
    }
}