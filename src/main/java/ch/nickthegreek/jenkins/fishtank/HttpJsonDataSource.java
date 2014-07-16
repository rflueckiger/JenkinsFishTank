package ch.nickthegreek.jenkins.fishtank;

import org.apache.commons.codec.binary.Base64;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public class HttpJsonDataSource implements JsonDataSource {

    public void loadData(InputStreamHandler handler) {
        InputStream inputStream = null;

        try {
            String host = Config.getInstance().getHost();
            String login = Config.getInstance().getLogin();
            String password = Config.getInstance().getToken();

            final URL url = new URL(String.format("%s/api/json?tree=jobs[name,color]", host));

            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + base64(login, password));

            inputStream = connection.getInputStream();

            handler.handle(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
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

    private static String base64(String username, String password) {
        return Base64.encodeBase64String(String.format("%s:%s", username, password).getBytes());
    }

}
