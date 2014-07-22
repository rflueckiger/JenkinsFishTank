package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SurfaceAnimation extends Animation {

    private final Color color;

    public SurfaceAnimation(Color color) {
        super(false);

        this.color = color;
    }

    @Override
    protected boolean doUpdate(long now, FishTankMetrics metrics) {
        double elapsedTime = now - getStartTime();
        double pixelsPerNanoSecond = 10d / (1000d * 1000d * 1000d);
        double distanceCovered = elapsedTime * pixelsPerNanoSecond;

        double newY = getFish().getY() - distanceCovered;
        double waterSurface = metrics.getAquaticBoundary().getMinY();
        if (newY <= waterSurface) {
            getFish().setY(waterSurface);
            return true;
        } else {
            getFish().setY(newY);
            return false;
        }
    }

    @Override
    protected void doDraw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(getFish().getX(), getFish().getY(), 10, 10);
    }

    public static SurfaceAnimation red() {
        return new SurfaceAnimation(Color.RED);
    }
}
