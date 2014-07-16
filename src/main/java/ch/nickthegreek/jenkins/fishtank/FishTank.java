package ch.nickthegreek.jenkins.fishtank;

import ch.nickthegreek.jenkins.fishtank.simplefish.SimpleFish;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FishTank {

    private Map<String, Fish> fishes = new HashMap<>();

    private final DoubleProperty widthProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty heightProperty = new SimpleDoubleProperty(0);

    private final Random rnd = new Random();

    private double airWaterRatio = 0.1;
    private Rectangle2D waterBoundary;

    private final ChangeListener<Number> updateWaterBoundaryListener = (observable, oldValue, newValue) -> {
        double waterOffset = getHeight() * airWaterRatio;
        double waterHeight = getHeight() - waterOffset;

        waterBoundary = new Rectangle2D(0, waterOffset, getWidth(), waterHeight);
    };

    public FishTank() {
        widthProperty().addListener(updateWaterBoundaryListener);
        heightProperty().addListener(updateWaterBoundaryListener);
    }

    public void addOrUpdateData(String name, FishState state) {
        Fish fish = fishes.get(name);
        if (fish == null) {
            fish = new SimpleFish(name, state);
            assignNewLocation(fish);
            fishes.put(name, fish);
        } else {
            fish.setState(state);
        }
    }

    public void assignNewLocation(Fish fish) {
        double waterOffset = getHeight() * airWaterRatio;
        double waterHeight = getHeight() - waterOffset;

        fish.setX(rnd.nextDouble() * getWidth());
        fish.setY(waterOffset + rnd.nextDouble() * waterHeight);
    }

    public void update(long now) {
        updateSelf(now);

        for (Fish fish : fishes.values()) {
            fish.update(now, waterBoundary);
        }
    }

    private void updateSelf(long now) {
        // TODO: add fancy fish tank animations (e.g. water surface waves)
    }

    public void drawAll(GraphicsContext gc) {
        drawSelf(gc);

        for (Fish fish : fishes.values()) {
            fish.draw(gc);
        }
    }

    public void drawSelf(GraphicsContext gc) {
        double airHeight = getHeight() * airWaterRatio;
        double waterHeight = getHeight() - airHeight;

        gc.setFill(Color.AQUA);
        gc.fillRect(0, airHeight, getWidth(), waterHeight);
    }

    public double getWidth() {
        return widthProperty.get();
    }

    public double getHeight() {
        return heightProperty.get();
    }

    public DoubleProperty widthProperty() {
        return widthProperty;
    }

    public DoubleProperty heightProperty() {
        return heightProperty;
    }

}
