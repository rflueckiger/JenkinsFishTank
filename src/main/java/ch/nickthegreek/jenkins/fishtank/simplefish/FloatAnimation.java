package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.Fish;
import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class FloatAnimation extends Animation {

    private final Random rnd = new Random();
    private final Color color;

    private long markTime;

    public FloatAnimation(Color color) {
        super(true);

        this.color = color;
    }

    @Override
    protected void doInit(long startTime, FishTankMetrics metrics) {
        markTime = startTime;
    }

    @Override
    protected boolean doUpdate(long now, FishTankMetrics metrics) {
        double elapsedTime = now - markTime;
        double pixelsPerNanoSecond = 1d / (1000d * 1000d * 1000d);
        double distanceCovered = elapsedTime * pixelsPerNanoSecond;

        double newX = getFish().getX() + ((rnd.nextBoolean() ? 1 : -1) * distanceCovered);

        newX = Math.min(newX, metrics.getAquaticBoundary().getMaxX());
        newX = Math.max(metrics.getAquaticBoundary().getMinX(), newX);

        getFish().setX(newX);

        markTime = now;
        return false;
    }

    @Override
    protected void doDraw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(getFish().getX(), getFish().getY(), 10, 10);
    }

    public static FloatAnimation red() {
        return new FloatAnimation(Color.RED);
    }
}
