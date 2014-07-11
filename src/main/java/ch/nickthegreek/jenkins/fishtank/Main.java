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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Executors;

public class Main extends Application {

    private JsonDataSource dataSource;
    private List<Job> jobs = Collections.emptyList();
    private Canvas canvas;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        dataSource = new HttpJsonDataSource();
//        dataSource = new FileJsonDataSource(new File("data.json"));
    }

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
        Executors.newSingleThreadExecutor().execute(() -> {
            getDataSource().loadData(inputStream -> {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonResponse jsonResponse = objectMapper.readValue(inputStream, JsonResponse.class);

                    Platform.runLater(() -> setJobs(jsonResponse.getJobs()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public JsonDataSource getDataSource() {
        return dataSource;
    }

    private void setJobs(List<Job> jobs) {
        this.jobs = jobs;

        // TODO: replace direct call with continuous drawing loop
        drawShapes();
    }

    private void drawShapes() {
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

}
