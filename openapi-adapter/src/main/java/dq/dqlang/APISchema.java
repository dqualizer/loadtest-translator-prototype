package dq.dqlang;

import com.fasterxml.jackson.annotation.JsonProperty;
import dq.dqlang.data.DataSchemas;
import dq.dqlang.field.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;

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
    private Field field;
    @JsonProperty("data_schemas")
    private DataSchemas dataSchemas;
}