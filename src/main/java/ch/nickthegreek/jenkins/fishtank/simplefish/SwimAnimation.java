package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public abstract class SwimAnimation extends Animation {

    private final Random rnd = new Random();

    private Point2D start;
    private Point2D target;

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
        // TODO: draw label?
        // TODO: fish moves in vertical directions should not rotate the fish much, expect when speed is high

        double angle = 0;
        if (getFish().getAngle() < 90 || getFish().getAngle() > 270) {
            angle = 0;
        } else if (getFish().getAngle() > 90 && getFish().getAngle() < 270) {
            // TODO: fish needs mirroring
            angle = 180;
        } else if (getFish().getAngle() == 90 || getFish().getAngle() == 270) {
            // TODO: use direction of last move
            angle = 0;
        }

        Image image = getFishImage();

        double x = getFish().getX() - image.getWidth() / 2;
        double y = getFish().getY() - image.getHeight() / 2;

        drawRotatedImage(gc, image, angle, x, y);
    }

    protected abstract Image getFishImage();

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
        double halfFishWidth = getFishImage().getWidth() / 2;
        double halfFishHeight = getFishImage().getHeight() / 2;

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

}
