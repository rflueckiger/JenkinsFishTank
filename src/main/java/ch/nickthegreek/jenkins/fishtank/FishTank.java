package ch.nickthegreek.jenkins.fishtank;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FishTank {

    private Map<String, Fish> fishes = new HashMap<>();

    private final DoubleProperty widthProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty heightProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty waterHeightProperty = new SimpleDoubleProperty(0);

    private final Random rnd = new Random();

    public void addOrUpdateData(String name, FishState state) {
        Fish fish = fishes.get(name);
        if (fish == null) {
            fish = new Fish(name, state);
            assignNewLocation(fish);
            fishes.put(name, fish);
        } else {
            fish.setState(state);
        }
    }

    public void assignNewLocation(Fish fish) {
        fish.setX(rnd.nextDouble() * getWidth());
        fish.setY((getHeight() - getWaterHeight()) + rnd.nextDouble() * getWaterHeight());
    }

    public double getWidth() {
        return widthProperty.get();
    }

    public double getHeight() {
        return heightProperty.get();
    }

    public double getWaterHeight() {
        return waterHeightProperty.get();
    }

    public DoubleProperty widthProperty() {
        return widthProperty;
    }

    public DoubleProperty heightProperty() {
        return heightProperty;
    }

    public DoubleProperty waterHeightProperty() {
        return waterHeightProperty;
    }

    public void drawAll(GraphicsContext gc) {
        for (Fish fish : fishes.values()) {
            fish.draw(gc);
        }
    }

}
