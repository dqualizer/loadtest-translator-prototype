package dq.mock.config;

import org.springframework.stereotype.Component;

@Component
public class PathConfig {

    private final String modeling = "modeling/modeling-sales.json";
    private final String resources = getResourcePath();

    public String getModeling() {
        return resources + modeling;
    }

    private String getResourcePath() {
        return this.getClass().getClassLoader()
                .getResource("")
                .getFile()
                .substring(1); //remove '/' at the beginning of the string
    }
}