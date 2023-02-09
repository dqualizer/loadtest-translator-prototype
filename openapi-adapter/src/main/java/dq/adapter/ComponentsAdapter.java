package dq.adapter;

import dq.dqlang.data.DataSchema;
import dq.dqlang.data.Property;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ComponentsAdapter {

    public Map<String, DataSchema> getDataSchemas(JSONObject componentsObject) {
        Map<String, DataSchema> dataSchemas = new LinkedHashMap<>();
        Set<String> componentTypes = componentsObject.keySet();

        for(String componentType: componentTypes) {
            JSONObject componentObject = componentsObject.getJSONObject(componentType);
            Set<String> component = componentObject.keySet();

            for(String dataName: component) {
                LinkedHashSet<Property> dataSchemaProperties = new LinkedHashSet<>();
                JSONObject dataObject = componentObject.getJSONObject(dataName);
                String dataType = dataObject.getString("type");
                JSONObject propertiesObject = dataObject.getJSONObject("properties");
                Set<String> properties = propertiesObject.keySet();

                for (String propertyName : properties) {
                    JSONObject propertyObject = propertiesObject.getJSONObject(propertyName);
                    String propertyType = "";
                    if(propertyObject.has("type")) propertyType = propertyObject.getString("type");
                    else if(propertyObject.has("$ref")) propertyType = propertyObject.getString("$ref");

                    Property dataSchemaProperty = new Property(propertyName, propertyType);
                    dataSchemaProperties.add(dataSchemaProperty);
                }
                DataSchema dataSchema = new DataSchema(dataType, dataSchemaProperties);
                dataSchemas.put(dataName, dataSchema);
            }
        }
        return dataSchemas;
    }
}