package dq.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PathConfig {

    private final String output = "output/apiSchema.json";
    private final String resources = getResourcePath();

    public String getOutput() {
        return resources + output;
    }

    private String getResourcePath() {
        return this.getClass().getClassLoader()
                .getResource("")
                .getFile()
                .substring(1); //remove '/' at the beginning of the string
    }
}