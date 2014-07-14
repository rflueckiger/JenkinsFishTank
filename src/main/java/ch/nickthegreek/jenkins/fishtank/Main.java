package ch.nickthegreek.jenkins.fishtank;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.DoubleExpression;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;

public class Main extends Application {

    private final FishTank fishTank = new FishTank();

    private JsonDataSource dataSource;
    private double airWaterRatio = 0.15;

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

        // TODO: add fullscreen option

        Group root = new Group();
        Scene scene = new Scene(root, 1024, 800);

        // create canvas
        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());

        fishTank.widthProperty().bind(canvas.widthProperty());
        fishTank.heightProperty().bind(canvas.heightProperty());
        fishTank.waterHeightProperty().bind(canvas.heightProperty().multiply(1 - airWaterRatio));

        GraphicsContext gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double width = gc.getCanvas().getWidth();
                double height = gc.getCanvas().getHeight();

                // update state of fishes
//                updateFishTank(width, height, now); // TODO: add fancy fish tank animations (e.g. water surface waves)
//                fishTank.udpate(now); // TODO: animate the fishes

                // clear screen
                gc.clearRect(0, 0, width, height);

                drawFishTank(gc, width, height);
                fishTank.drawAll(gc);
            }
        }.start();

        // add canvas to scene
        root.getChildren().add(canvas);

        primaryStage.setScene(scene);
        primaryStage.show();

        // load data
        refreshData();
    }

    private void drawFishTank(GraphicsContext gc, double width, double height) {
        double airHeight = height * airWaterRatio;
        double waterHeight = height - airHeight;

        gc.setFill(Color.AQUA);
        gc.fillRect(0, airHeight, width, waterHeight);
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
        // TODO: this should probably be done from the loader thread
        // TODO: sync every job by itself into the model, so the drawing loop only stopped for short periods of time
        for (Job job : jobs) {
            fishTank.addOrUpdateData(job.getName(), deriveState(job.getColor()));
        }
    }

    private FishState deriveState(String color) {
        // TODO: add mappings from all the other jenkins colors to fish states.
        if ("blue".equals(color)) {
            return FishState.ALIVE;
        } else if ("red".equals(color)) {
            return FishState.DEAD;
        } else {
            return FishState.GHOST;
        }
    }

}
