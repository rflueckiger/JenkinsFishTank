package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.Fish;
import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SurfaceAnimation extends Animation {

    private final Fish fish;
    private final Color color;

    public SurfaceAnimation(Fish fish, Color color) {
        super(false);

        this.fish = fish;
        this.color = color;
    }

    @Override
    protected boolean doUpdate(long now, FishTankMetrics metrics) {
        double elapsedTime = now - getStartTime();
        double pixelsPerNanoSecond = 10d / (1000d * 1000d * 1000d);
        double distanceCovered = elapsedTime * pixelsPerNanoSecond;

        double newY = fish.getY() - distanceCovered;
        double waterSurface = metrics.getAquaticBoundary().getMinY();
        if (newY <= waterSurface) {
            fish.setY(waterSurface);
            return true;
        } else {
            fish.setY(newY);
            return false;
        }
    }

    @Override
    protected void doDraw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(fish.getX(), fish.getY(), 10, 10);
    }
}
