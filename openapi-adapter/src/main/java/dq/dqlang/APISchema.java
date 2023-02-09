package dq.dqlang;

import com.fasterxml.jackson.annotation.JsonProperty;
import dq.dqlang.data.DataSchema;
import dq.dqlang.field.FieldItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class APISchema {

    private final int version = 1;
    private String context;
    private String api;
    @JsonProperty("server_info")
    private LinkedHashSet<ServerInfo> serverInfo;
    private LinkedHashSet<FieldItem> field;
    @JsonProperty("data_schemas")
    private Map<String, DataSchema> dataSchemas;
}