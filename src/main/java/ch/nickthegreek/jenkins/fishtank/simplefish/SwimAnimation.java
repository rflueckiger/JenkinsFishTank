package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public abstract class SwimAnimation extends Animation {

    private final Random rnd = new Random();

    private Point2D start;
    private Point2D target;
    private boolean labelEnabled = true;

    public SwimAnimation() {
        super(true);
    }

    @Override
    protected void doInit(long startTime, FishTankMetrics metrics) {
        start = new Point2D(getFish().getX(), getFish().getY());

        // set a target location for the move
        double xRange = rnd.nextGaussian() * 100 * (rnd.nextBoolean() ? 1 : -1);
        double yRange = rnd.nextGaussian() * 50 * (rnd.nextBoolean() ? 1 : -1);
        target = new Point2D(start.getX() + xRange, start.getY() + yRange);
    }

    @Override
    protected void doDraw(GraphicsContext gc) {
        Image image = null;

        double angle = 0;
        if (getFish().getAngle() < 90 || getFish().getAngle() > 270) {
            image = getFishImageL();
            angle = 0;
        } else if (getFish().getAngle() > 90 && getFish().getAngle() < 270) {
            image = getFishImageR();
            angle = 180;
        } else if (getFish().getAngle() == 90 || getFish().getAngle() == 270) {
            image = getFishImageL();
            angle = 0;
        }

        double x = getFish().getX() - image.getWidth() / 2;
        double y = getFish().getY() - image.getHeight() / 2;

        drawRotatedImage(gc, image, 0, x, y);

        if (isLabelEnabled()) {
            drawLabel(gc);
        }
    }

    protected abstract Image getFishImageL();
    protected abstract Image getFishImageR();

    @Override
    protected boolean doUpdate(long now, FishTankMetrics metrics) {
        double xDelta = target.getX() - start.getX();
        double yDelta = target.getY() - start.getY();
        double distance = Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
        double angle = (Math.atan2(yDelta, xDelta) + Math.PI) / Math.PI * 180;
        double pixelsPerNanoSecond = 10d / (1000d * 1000d * 1000d);
        double moveDuration = distance / pixelsPerNanoSecond;

        double elapsedTime = now - getStartTime();

        elapsedTime = Math.min(moveDuration, elapsedTime);

        double factor = elapsedTime / moveDuration;
        double currentX = start.getX() + (xDelta * factor);
        double currentY = start.getY() + (yDelta * factor);

        Rectangle2D boundary = metrics.getAquaticBoundary();

        // if the fish moves outside of the boundaries, fix it and interrupt the move
        boolean finished = factor >= 1;
        double halfFishWidth = getFishImageL().getWidth() / 2;
        double halfFishHeight = getFishImageL().getHeight() / 2;

        if (currentX < boundary.getMinX() + halfFishWidth) {
            currentX = boundary.getMinX() + halfFishWidth;
            finished = true;
        }
        if (currentY < boundary.getMinY() + halfFishHeight) {
            currentY = boundary.getMinY() + halfFishHeight;
            finished = true;
        }
        if (currentX > boundary.getMaxX() - halfFishWidth) {
            currentX = boundary.getMaxX() - halfFishWidth;
            finished = true;
        }
        if (currentY > boundary.getMaxY() - halfFishHeight) {
            currentY = boundary.getMaxY() - halfFishHeight;
            finished = true;
        }

        getFish().setX(currentX);
        getFish().setY(currentY);
        getFish().setAngle(angle);

        return finished;
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

    public void setLabelEnabled(boolean labelEnabled) {
        this.labelEnabled = labelEnabled;
    }

    public boolean isLabelEnabled() {
        return labelEnabled;
    }
}
