package ch.nickthegreek.jenkins.fishtank.util;

import ch.nickthegreek.jenkins.fishtank.JsonDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileJsonDataSource implements JsonDataSource {

    private final File file;

    public FileJsonDataSource(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null.");
        }
        this.file = file;
    }

    @Override
    public void loadData(InputStreamHandler handler) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            handler.handle(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
