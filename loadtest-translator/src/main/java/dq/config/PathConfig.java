package dq.config;

import org.springframework.stereotype.Component;

@Component
public class PathConfig {

    private final String mapping = "mapping/mapping-werkstatt.json";
    private final String resources = getResourcePath();

    public String getMapping() {
        return resources + mapping;
    }

    private String getResourcePath() {
        return this.getClass().getClassLoader()
                .getResource("")
                .getFile()
                .substring(1); //remove '/' at the beginning of the string
    }
}