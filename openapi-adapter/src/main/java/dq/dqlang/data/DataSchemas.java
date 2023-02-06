package dq.dqlang.data;

import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@ToString
public class DataSchemas {

    private Map<String, DataSchema> schemas;

    public DataSchemas addSchemas(String name, DataSchema dataSchema) {
        if (this.schemas == null) {
            this.schemas = new LinkedHashMap<>();
        }
        this.schemas.put(name, dataSchema);
        return this;
    }
}