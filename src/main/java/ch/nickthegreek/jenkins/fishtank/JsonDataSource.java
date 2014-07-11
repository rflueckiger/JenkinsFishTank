package ch.nickthegreek.jenkins.fishtank;

import java.io.InputStream;

public interface JsonDataSource {

    public interface InputStreamHandler {
        void handle(InputStream inputStream);
    }

    void loadData(InputStreamHandler handler);

}
