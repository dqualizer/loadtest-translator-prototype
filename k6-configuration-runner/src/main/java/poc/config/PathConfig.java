package poc.config;

import org.springframework.stereotype.Component;

@Component
public class PathConfig {

    private final String script = "poc/scripts/";
    private final String logging = "poc/output/logging.txt";
    private final String resources = getResourcePath();


    public String getScriptFolder() { return resources + script; }

    public String getLogging() { return resources + logging; }

    public String getResourcePath() {
        return this.getClass().getClassLoader()
                .getResource("")
                .getFile()
                .substring(1); //remove '/' at the beginning of the string
    }
}