package ch.nickthegreek.jenkins.fishtank;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
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
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {

        JsonResponse jsonResponse = null;
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("config.properties"));

            String login = config.getProperty("login");
            String password = config.getProperty("token");

            final URL url = new URL("https://ci.inventage.com/api/json?tree=jobs[name,color]");
            final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestProperty("Authorization", "Basic " + base64(login, password));

            ObjectMapper objectMapper = new ObjectMapper();
            jsonResponse = objectMapper.readValue(httpURLConnection.getInputStream(), JsonResponse.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//            }
//        });

        Group root = new Group();
        Canvas canvas = new Canvas(1024, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc, jsonResponse);

        root.getChildren().add(canvas);

        primaryStage.setScene(new Scene(root, 1024, 800));
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc, JsonResponse response) {
        gc.setFill(Color.AQUA);
        gc.fillRect(0, 50, gc.getCanvas().getWidth(), gc.getCanvas().getHeight() - 50);

        Random rnd = new Random();
        for (Job job : response.getJobs()) {
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
            return Color.GREY;
        }
    }

//        gc.setFill(Color.GREEN);
//        gc.setStroke(Color.BLUE);
//        gc.setLineWidth(5);
//        gc.strokeLine(40, 10, 10, 40);
//        gc.fillOval(10, 60, 30, 30);
//        gc.strokeOval(60, 60, 30, 30);
//        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
//        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
//        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
//        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
//        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
//        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
//        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
//        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
//        gc.fillPolygon(new double[]{10, 40, 10, 40},
//                new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolygon(new double[]{60, 90, 60, 90},
//                new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolyline(new double[]{110, 140, 110, 140},
//                new double[]{210, 210, 240, 240}, 4);

    private static String base64(String username, String password) {
        return Base64.encodeBase64String(String.format("%s:%s", username, password).getBytes());
    }

    private static class JsonResponse {
        private List<Job> jobs;

        public List<Job> getJobs() {
            return jobs;
        }

        public void setJobs(List<Job> jobs) {
            this.jobs = jobs;
        }
    }

    private static class Job {
        private String name;
        private String color;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
