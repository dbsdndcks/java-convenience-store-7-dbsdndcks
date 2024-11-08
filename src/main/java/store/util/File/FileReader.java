package store.util.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.function.Function;

public class FileReader {

    public <T> List<T> readFile(String filePath, Function<String, T> lineParser) {
        try (InputStream inputStream = getResourceStream(filePath)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines().skip(1)
                    .map(lineParser)
                    .toList();
        } catch (IOException e) {
            throw new IllegalArgumentException("File reading error: " + e.getMessage());
        }
    }

    private InputStream getResourceStream(String file) throws IOException {
        URL resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IOException("Resource not found: " + file);
        }
        return resource.openStream();
    }
}
