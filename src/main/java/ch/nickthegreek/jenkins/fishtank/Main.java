package ch.nickthegreek.jenkins.fishtank;


import ch.nickthegreek.jenkins.fishtank.util.FileJsonDataSource;
import ch.nickthegreek.jenkins.fishtank.util.FishEvent;
import ch.nickthegreek.jenkins.fishtank.util.FishEventScript;
import ch.nickthegreek.jenkins.fishtank.util.FishEventScriptLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        // create canvas
        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
        canvas.setOnMouseClicked(event -> {
            System.out.println(String.format("clicked: (%s, %s)", event.getX(), event.getY()));
        });

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

        // poll for data
        FishTankUpdateJob fishTankUpdateJob = new FishTankUpdateJob(getDataSource(), fishTank);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(fishTankUpdateJob, 0, 30, TimeUnit.SECONDS);
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

    public JsonDataSource getDataSource() {
        return dataSource;
    }

}
