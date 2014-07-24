package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.canvas.GraphicsContext;

public class SurfaceAnimation extends Animation {

    public SurfaceAnimation() {
        super(false);
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
        drawRotatedImage(gc, FishImages.getDeadImage(), 0, getFish().getX(), getFish().getY());
    }

    public static SurfaceAnimation create() {
        return new SurfaceAnimation();
    }
}
