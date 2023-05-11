package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

    private File file;

    public CSVWriter(String filename) {
        file = new File(filename);
    }

    public boolean fileExists() {
        return file.exists();
    }

    public void write(String content) throws IOException {
        FileWriter writer = new FileWriter(file, false); // false means overwrite file
        writer.write(content);
        writer.close();
    }

}