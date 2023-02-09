package dq.config;

import org.springframework.stereotype.Component;

@Component
public class PathConfig {

    private final String constants = "constant/constants.json";
    private final String resources = getResourcePath();

    public String getConstants() {
        return resources + constants;
    }

    private String getResourcePath() {
        return this.getClass().getClassLoader()
                .getResource("")
                .getFile()
                .substring(1); //remove '/' at the beginning of the string
    }
}