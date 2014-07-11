package ch.nickthegreek.jenkins.fishtank;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Executors;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private List<Job> jobs = Collections.emptyList();
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JenkinsFishTank");

        // TODO: canvas must grow with scene
        // TODO: fullscreen support

        Group root = new Group();
        canvas = new Canvas(1024, 800);

        root.getChildren().add(canvas);

        primaryStage.setScene(new Scene(root, 1024, 800));
        primaryStage.show();

        refreshData();
    }

    private void refreshData() {
        // TODO: extract to class
        // TODO: dump json data from server to file and add a dummy loader for offline testing
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Properties config = new Properties();
                    config.load(new FileInputStream("config.properties"));

                    String host = config.getProperty("host");
                    String login = config.getProperty("login");
                    String password = config.getProperty("token");

                    final URL url = new URL(String.format("%s/api/json?tree=jobs[name,color]", host));
                    final URLConnection connection = url.openConnection();

                    connection.setRequestProperty("Authorization", "Basic " + base64(login, password));

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonResponse jsonResponse = objectMapper.readValue(connection.getInputStream(), JsonResponse.class);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            setJobs(jsonResponse.getJobs());
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setJobs(List<Job> jobs) {
        this.jobs = jobs;

        // TODO: replace direct call with continous drawing loop
        drawShapes(jobs);
    }

    private void drawShapes(List<Job> jobs) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.AQUA);
        gc.fillRect(0, 50, gc.getCanvas().getWidth(), gc.getCanvas().getHeight() - 50);

        Random rnd = new Random();
        for (Job job : jobs) {
            double x = rnd.nextDouble() * gc.getCanvas().getWidth();
            double y = rnd.nextDouble() * gc.getCanvas().getHeight();

            gc.setFill(getColor(job.getColor()));
            gc.fillOval(x, y, 10, 10);
            gc.fillText(job.getName(), x, y);
        }
    }

    private Paint getColor(String color) {
        if ("blue".equals(color)) {
            return Color.BLUE;
        } else if ("red".equals(color)) {
            return Color.RED;
        } else {
            // TODO: handle all the other states
            return Color.GREY;
        }
    }

    private static String base64(String username, String password) {
        return Base64.encodeBase64String(String.format("%s:%s", username, password).getBytes());
    }

}
