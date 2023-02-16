package dq.mock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathConfig {

    @Value("${modeling.file:modeling/modeling-werkstatt.json}")
    private String modeling;
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