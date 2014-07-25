package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

public class SurfaceAnimation extends Animation {

    private static final Random RND = new Random();

    private double pixelsPerNanoSecond;
    private double markTime;

    public SurfaceAnimation() {
        super(false);
    }

    @Override
    protected void doInit(long startTime, FishTankMetrics metrics) {
        markTime = startTime;

        pixelsPerNanoSecond = (2 + RND.nextDouble() * 5) / (1000d * 1000d * 1000d);
    }

    @Override
    protected boolean doUpdate(long now, FishTankMetrics metrics) {
        double elapsedTime = now - markTime;
        double distanceCovered = elapsedTime * pixelsPerNanoSecond;

        markTime = now;

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

    protected Image getFishImageL() {
        return FishImages.getDeadImageL();
    }

    @Override
    protected void doDraw(GraphicsContext gc) {
        double x = getFish().getX() - getFishImageL().getWidth() / 2;
        double y = getFish().getY() - getFishImageL().getHeight() / 2;

        drawRotatedImage(gc, FishImages.getDeadImageL(), 0, x, y);
        drawLabel(gc);
    }

    protected void drawLabel(GraphicsContext gc) {
        gc.save();
        gc.setFont(Font.font(9));
        gc.setFill(Color.BLACK);

        float textExtent = Toolkit.getToolkit().getFontLoader().computeStringWidth(getFish().getName(), gc.getFont());

        double x = getFish().getX() - textExtent / 2;
        double y = getFish().getY() + getFishImageL().getHeight() / 2 + 10;

        gc.fillText(getFish().getName(), x, y);
        gc.restore();
    }

    public static SurfaceAnimation create() {
        return new SurfaceAnimation();
    }
}
