package dq.dqlang.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashSet;

/**
 * Java class for the mapping representation
 */
@Getter
@ToString
public class Mapping {

    private long version;
    private String context;
    @JsonProperty("server_info")
    private LinkedHashSet<ServerInfo> serverInfo;
    private LinkedHashSet<MappingObject> objects;
}