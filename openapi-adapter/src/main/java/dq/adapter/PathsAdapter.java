package dq.adapter;

import dq.dqlang.field.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class PathsAdapter {

    public Field getField(JSONObject pathsObject) {
        LinkedHashSet<FieldItem> fieldItems = new LinkedHashSet<>();
        Set<String> paths = pathsObject.keySet();

        for(String path : paths) {
            JSONObject pathObject = pathsObject.getJSONObject(path);
            Set<String> operations = pathObject.keySet();

            for(String operation: operations) {
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
        for(int i = 0; i < parameters.length(); i++) {
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
        JSONObject schema = requestBody.getJSONObject("content").getJSONObject("application/json")
                .getJSONObject("schema");
        String bodyType = "";
        if(schema.has("$ref")) bodyType = schema.getString("$ref");
        else if(schema.has("type")) bodyType = schema.getString("type");

        return new Body(bodyType);
    }

    private LinkedHashSet<Output> getOutput(JSONObject operationObject) {
        LinkedHashSet<Output> outputs = new LinkedHashSet<>();
        if(!operationObject.has("responses")) return outputs;

        JSONObject responsesObject = operationObject.getJSONObject("responses");
        Set<String> responses = responsesObject.keySet();

        for(String response : responses) {
            JSONObject oneResponse = responsesObject.getJSONObject(response);
            JSONObject schema = oneResponse.getJSONObject("content").getJSONObject("application/json")
                    .getJSONObject("schema");
            String type = "";
            if(schema.has("$ref")) type = schema.getString("$ref");
            else if(schema.has("type")) type = schema.getString("type");

            Output output = new Output(type, response);
            outputs.add(output);
        }
        return outputs;
    }
}