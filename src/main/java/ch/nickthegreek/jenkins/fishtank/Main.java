package ch.nickthegreek.jenkins.fishtank;


import ch.nickthegreek.jenkins.fishtank.util.FileJsonDataSource;
import ch.nickthegreek.jenkins.fishtank.util.FishEvent;
import ch.nickthegreek.jenkins.fishtank.util.FishEventScript;
import ch.nickthegreek.jenkins.fishtank.util.FishEventScriptLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;

public class Main extends Application {

    private final FishTank fishTank = new FishTank();

    private JsonDataSource dataSource;
    private FishEventScript script;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        // init data source
        String testData = Config.getInstance().getTestData();
        if (testData != null) {
            dataSource = new FileJsonDataSource(new File(testData));
        } else {
            dataSource = new HttpJsonDataSource();
        }

        // init script if configured
        String testScript = Config.getInstance().getTestScript();
        if (testScript != null) {
            script = new FishEventScriptLoader(new File(testScript)).getFishEventScript();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JenkinsFishTank");

        primaryStage.setWidth(1024);
        primaryStage.setHeight(800);
        primaryStage.setFullScreen(false);

        // create canvas
        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());

        fishTank.widthProperty().bind(canvas.widthProperty());
        fishTank.heightProperty().bind(canvas.heightProperty());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                handleScriptedEvents(now);

                GraphicsContext gc = canvas.getGraphicsContext2D();

                double width = gc.getCanvas().getWidth();
                double height = gc.getCanvas().getHeight();

                // 1. update
                fishTank.update(now);

                // 2. clear
                gc.clearRect(0, 0, width, height);

                // 3. draw
                fishTank.drawAll(gc);
            }
        };

        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.show();

        timer.start();

        // load initial data (later: start data polling thread)
        refreshData();
    }

    private void handleScriptedEvents(long now) {
        if (script != null) {
            if (!script.isStarted()) {
                script.start(now);
            }

            List<FishEvent> events = script.getEvents(now);
            for (FishEvent event : events) {
                fishTank.addOrUpdateData(event.getName(), event.getState());
            }
        }
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
        // TODO: sync every job by itself into the model, so the drawing loop is only stopped for short periods of time
        for (Job job : jobs) {
            fishTank.addOrUpdateData(job.getName(), deriveState(job.getColor()));
        }
    }

    private FishState deriveState(String color) {
        if ("blue".equals(color)) {
            return FishState.ALIVE;
        } else if ("blue_anime".equals(color)) {
            return FishState.ALIVE_PENDING;
        } else if ("red".equals(color)) {
            return FishState.DEAD;
        } else if ("red_anime".equals(color)) {
            return FishState.DEAD_PENDING;
        } else if ("yellow".equals(color)) {
            return FishState.SICK;
        } else if ("yellow_anime".equals(color)) {
            return FishState.SICK_PENDING;
        } else {
            return FishState.GHOST;
        }
    }

}
