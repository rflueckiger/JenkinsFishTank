package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SurfaceAnimation extends Animation {

    private final double pixelsPerNanoSecond = 0.01d / (1000d * 1000d * 1000d);

    public SurfaceAnimation() {
        super(false);
    }

    @Override
    protected boolean doUpdate(long now, FishTankMetrics metrics) {
        double elapsedTime = now - getStartTime();
        double distanceCovered = elapsedTime * pixelsPerNanoSecond;

        double newY = getFish().getY() - distanceCovered;
        double waterSurface = metrics.getAquaticBoundary().getMinY();
        if (newY <= waterSurface - getFishImageL().getHeight() / 2) {
            getFish().setY(waterSurface - getFishImageL().getHeight() / 2);
            return true;
        } else {
            getFish().setY(newY);
            return false;
        }
    }

    protected Image getFishImageL() {
        return FishImages.getDeadImageL();
    }

    @Override
    protected void doDraw(GraphicsContext gc) {
        drawRotatedImage(gc, FishImages.getDeadImageL(), 0, getFish().getX(), getFish().getY());
    }

    public static SurfaceAnimation create() {
        return new SurfaceAnimation();
    }
}
