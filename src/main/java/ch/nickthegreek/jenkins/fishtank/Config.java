package ch.nickthegreek.jenkins.fishtank;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Config instance;

    private final Properties config;

    public Config() {
        config = new Properties();
        try {
            config.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getHost() {
        return config.getProperty("host");
    }

    public String getLogin() {
        return config.getProperty("login");
    }

    public String getToken() {
        return config.getProperty("token");
    }

    public String getTestData() {
        return config.getProperty("test.data");
    }

    public String getTestScript() {
        return config.getProperty("test.script");
    }

    public String getTestTracerFish() {return config.getProperty("test.tracerFish"); }

}
