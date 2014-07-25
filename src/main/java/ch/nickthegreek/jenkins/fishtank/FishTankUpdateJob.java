package ch.nickthegreek.jenkins.fishtank;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Random;

public class FishTankUpdateJob implements Runnable {

    private final JsonDataSource dataSource;
    private final FishTank fishTank;

    public FishTankUpdateJob(JsonDataSource dataSource, FishTank fishTank) {
        this.dataSource = dataSource;
        this.fishTank = fishTank;
    }

    @Override
    public void run() {
        System.out.println("updating fish tank data...");

        fishTank.dataQualityStateProperty().setValue(DataQualityState.Updating);
        dataSource.loadData(inputStream -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonResponse jsonResponse = objectMapper.readValue(inputStream, JsonResponse.class);

                Random rnd = new Random();
                if (rnd.nextBoolean()) {
                    throw new RuntimeException("bäm");
                }

                for (Job job : jsonResponse.getJobs()) {
                    Platform.runLater(() -> fishTank.addOrUpdateData(job.getName(), deriveState(job.getColor())));
                }

                fishTank.dataQualityStateProperty().setValue(DataQualityState.UpToDate);
            } catch (Exception e) {
                fishTank.dataQualityStateProperty().setValue(DataQualityState.OutOfDate);
                Platform.runLater(() -> fishTank.handleException(e));
            }
        });
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
