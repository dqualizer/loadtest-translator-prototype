package dq.adapter;

import dq.dqlang.data.DataSchema;
import dq.dqlang.data.DataSchemas;
import dq.dqlang.data.Property;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class ComponentsAdapter {

    public DataSchemas getDataSchemas(JSONObject componentsObject) {
        DataSchemas dataSchemas = new DataSchemas();
        Set<String> componentTypes = componentsObject.keySet();

        for(String componentType: componentTypes) {
            JSONObject componentObject = componentsObject.getJSONObject(componentType);
            Set<String> component = componentObject.keySet();

            for(String data: component) {
                LinkedHashSet<Property> dataSchemaproperties = new LinkedHashSet<>();
                JSONObject dataObject = componentObject.getJSONObject(data);
                JSONObject propertiesObject = dataObject.getJSONObject("properties");
                Set<String> properties = propertiesObject.keySet();

                for (String property : properties) {
                    JSONObject propertyObject = propertiesObject.getJSONObject(property);
                    String type = "";
                    if(propertyObject.has("type")) type = propertyObject.getString("type");
                    else if(propertyObject.has("$ref")) type = propertyObject.getString("$ref");

                    Property dataSchemaProperty = new Property(property, type);
                    dataSchemaproperties.add(dataSchemaProperty);
                }
                DataSchema dataSchema = new DataSchema(dataSchemaproperties);
                dataSchemas.addSchemas(data, dataSchema);
            }
        }
        return dataSchemas;
    }
}