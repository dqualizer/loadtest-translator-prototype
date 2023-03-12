package poc.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration for local file paths
 */
@Configuration
public class PathConfig {

    private final String script = "poc/scripts/createdScript";
    private final String logging = "poc/logging/logging";
    private final String resources = getResourcePath();

    public String getScript(int counter) {
        return resources + script + counter + ".js";
    }

    public String getLogging(int counter1, int counter2) {
        return resources + logging + counter1 + "-" + counter2 + ".txt";
    }

    public String getResourcePath() {
        return this.getClass().getClassLoader()
                .getResource("")
                .getFile()
                .substring(1); //remove '/' at the beginning of the string
    }
}