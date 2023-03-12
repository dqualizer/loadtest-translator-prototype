package poc.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Helps to read a local file and get the content as a string
 * ( Handle the try-catch-block )
 */
@Component
public class MyFileReader {

    public String readFile(String path) {
        String text;
        try { text = Files.readString(Paths.get(path)); }
        catch (IOException e) { throw new RuntimeException(e); }
        return text;
    }
}