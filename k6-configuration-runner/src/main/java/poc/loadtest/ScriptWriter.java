package poc.loadtest;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class ScriptWriter {

    public void write(List<String> script, String scriptPath) throws IOException {
        FileWriter writer = new FileWriter(scriptPath);

        for(String line: script) writer.write(line);
        writer.close();
    }
}