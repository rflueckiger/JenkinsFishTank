package ch.nickthegreek.jenkins.fishtank.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamFileWriter {

    private final File file;

    public InputStreamFileWriter(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file must not null.");
        }
        this.file = file;
    }

    public void write(InputStream inputStream) {
        FileOutputStream outputStream = null;

        try {
            System.out.println(String.format("Writing InputStream to file: %s", file.getAbsolutePath()));
            outputStream = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            System.out.println("Writing file successful!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
