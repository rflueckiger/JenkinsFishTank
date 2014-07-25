package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.Fish;
import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

import java.util.Random;

import static ch.nickthegreek.jenkins.fishtank.FishState.DEAD;
import static ch.nickthegreek.jenkins.fishtank.FishState.DEAD_PENDING;

public class FloatAnimation extends Animation {

    private final Random rnd = new Random();

    private long markTime;
    private final double pixelsPerNanoSecond = 0.01d / (1000d * 1000d * 1000d);

    public FloatAnimation() {
        super(true);
    }

    @Override
    protected void doInit(long startTime, FishTankMetrics metrics) {
        markTime = startTime;
    }

    @Override
    protected boolean doUpdate(long now, FishTankMetrics metrics) {
        // TODO: floating should mean stay at the surface - even if the surface changes its height :-)

        double elapsedTime = now - markTime;
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
        gc.save();

        double x = getFish().getX() - getFishImageL().getWidth() / 2;
        double y = getFish().getY() - getFishImageL().getHeight() / 2;

        drawRotatedImage(gc, getFishImageL(), 0, x, y);

        gc.setFont(Font.font(9));
        gc.setFill(Color.GREY);
        drawRotatedText(gc, getFish().getName(), -45, getFish().getX(), getFish().getY() - getFishImageL().getHeight() / 2);

        gc.restore();
    }

    protected Image getFishImageL() {
        return FishImages.getDeadImageL();
    }

    public static FloatAnimation create() {
        return new FloatAnimation();
    }
}
