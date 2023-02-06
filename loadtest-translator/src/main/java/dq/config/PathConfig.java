package dq.config;

import org.springframework.stereotype.Component;

@Component
public class PathConfig {

    private final String mappingFolder = "mapping";
    private final String resources = getResourcePath();

    public String getMappingFolder() {
        return resources + mappingFolder;
    }

    private String getResourcePath() {
        return this.getClass().getClassLoader()
                .getResource("")
                .getFile()
                .substring(1); //remove '/' at the beginning of the string
    }
}