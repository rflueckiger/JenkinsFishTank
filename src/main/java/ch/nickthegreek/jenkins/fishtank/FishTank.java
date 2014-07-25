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

public class FishTank implements FishTankMetrics {

    private static final int PLANE_COUNT = 3;

    private Map<String, Fish> fishes = new HashMap<>();

    private final DoubleProperty widthProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty heightProperty = new SimpleDoubleProperty(0);

    private final Random rnd = new Random();

    private double airWaterRatio = 0.15;

    private Rectangle2D boundary;
    private Rectangle2D aquaticBoundary;

    private final ChangeListener<Number> updateAquaticBoundaryListener = (observable, oldValue, newValue) -> {
        double waterOffset = getHeight() * airWaterRatio;
        double waterHeight = getHeight() - waterOffset;

        boundary = new Rectangle2D(0, 0, getWidth(), getHeight());
        aquaticBoundary = new Rectangle2D(0, waterOffset, getWidth(), waterHeight);

        System.out.println(String.format("fish tank boundary: %s", boundary));
        System.out.println(String.format("fish tank aquatic boundary: %s", aquaticBoundary));
    };

    public FishTank() {
        widthProperty().addListener(updateAquaticBoundaryListener);
        heightProperty().addListener(updateAquaticBoundaryListener);
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
        fish.setAngle(rnd.nextDouble() * 360);
        fish.setPlane(rnd.nextInt(PLANE_COUNT));
    }

    public void update(long now) {
emor        for (Fish fish : fishes.values()) {
            fish.update(now, this);
        }
    }

    public void drawAll(GraphicsContext gc) {
        drawSelf(gc);

        // draw fish last plane first
        for (int plane = 0; plane < PLANE_COUNT; plane++) {
            for (Fish fish : fishes.values()) {
                if (fish.getPlane() == plane) {
                    fish.draw(gc);
                }
            }
            drawPlaneForeground(gc, plane);
        }
    }

    protected void drawPlaneForeground(GraphicsContext gc, int plane) {
        gc.save();
        gc.setFill(Color.AQUA);
        gc.setGlobalAlpha(0.3);
        gc.fillRect(aquaticBoundary.getMinX(), aquaticBoundary.getMinY(), aquaticBoundary.getWidth(), aquaticBoundary.getHeight());
        gc.restore();
    }

    public void drawSelf(GraphicsContext gc) {
        gc.setFill(Color.AQUA);
        gc.fillRect(aquaticBoundary.getMinX(), aquaticBoundary.getMinY(), aquaticBoundary.getWidth(), aquaticBoundary.getHeight());
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

    @Override
    public Rectangle2D getAquaticBoundary() {
        return aquaticBoundary;
    }

    @Override
    public Rectangle2D getBoundary() {
        return boundary;
    }
}
