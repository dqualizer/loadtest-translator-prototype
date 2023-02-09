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

@Component
public class OpenAPIAdapter {

    @Value("${api.context}")
    private String context;
    @Autowired
    private PathsAdapter pathsAdapter;
    @Autowired
    private ComponentsAdapter componentsAdapter;

    public APISchema adapt(JSONObject openAPISchema) {
        String api = "OpenAPI_" + openAPISchema.getString("openapi");
        LinkedHashSet<ServerInfo> serverInfo = this.getServerInfo(openAPISchema.getJSONArray("servers"));
        LinkedHashSet<FieldItem> field = pathsAdapter.getField(openAPISchema.getJSONObject("paths"));
        Map<String, DataSchema> schemas = componentsAdapter.getDataSchemas(openAPISchema.getJSONObject("components"));

        APISchema apiSchema = new APISchema(context, api, serverInfo, field, schemas);
        return apiSchema;
    }

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