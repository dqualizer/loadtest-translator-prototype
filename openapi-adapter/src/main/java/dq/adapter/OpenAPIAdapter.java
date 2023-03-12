package dq.adapter;

import dq.dqlang.APISchema;
import dq.dqlang.ServerInfo;
import dq.dqlang.data.DataSchema;
import dq.dqlang.field.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Adapts an OpenAPI-schema to a dqlang-API-schema
 */
@Component
public class OpenAPIAdapter {

    //Since the OpenAPI-schema does not include a bounded context, it has to be defined externally
    @Value("${api.context}")
    private String context;

    @Autowired
    private PathsAdapter pathsAdapter;
    @Autowired
    private ComponentsAdapter componentsAdapter;

    /**
     * Adapt the API-schema. It consists of three steps:
     * 1. Adapt the serverInfo
     * 2. Adapt the Paths
     * 3. Adapt the Components (data-schema)
     * @param openAPISchema OpenAPI-schema of an application
     * @return Adapted dqlang-API-schema
     */
    public APISchema adapt(JSONObject openAPISchema) {
        String api = "OpenAPI_" + openAPISchema.getString("openapi");
        LinkedHashSet<ServerInfo> serverInfo = this.getServerInfo(openAPISchema.getJSONArray("servers"));
        LinkedHashSet<FieldItem> field = pathsAdapter.getField(openAPISchema.getJSONObject("paths"));
        Map<String, DataSchema> schemas = componentsAdapter.getDataSchemas(openAPISchema.getJSONObject("components"));

        APISchema apiSchema = new APISchema(context, api, serverInfo, field, schemas);
        return apiSchema;
    }

    /**
     * Adapt the server information
     * @param servers A list with all server information inside the OpenAPI-schema
     * @return An adapted server information
     */
    private LinkedHashSet<ServerInfo> getServerInfo(JSONArray servers) {
        LinkedHashSet<ServerInfo> infos = new LinkedHashSet<>();

        for(int i = 0; i < servers.length(); i++) {
            JSONObject server = servers.getJSONObject(i);
            String url = server.getString("url");
            String description = server.getString("description");

            ServerInfo info = new ServerInfo(url, description);
            infos.add(info);
        }
        return infos;
    }
}