package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class SwimAnimation extends Animation {

    private final Color color;
    private final Random rnd = new Random();

    private Point2D start;
    private Point2D target;

    public SwimAnimation(Color color) {
        super(true);

        this.color = color;
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
        gc.setFill(color);
        gc.fillOval(getFish().getX(), getFish().getY(), 10, 10);
    }

    @Override
    protected boolean doUpdate(long now, FishTankMetrics metrics) {
        // TODO: consider the size of the fish (currently, everything drawn to the left of x,y is still outside the boundary)
        double xDelta = target.getX() - start.getX();
        double yDelta = target.getY() - start.getY();
        double distance = Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
        double pixelsPerNanoSecond = 10d / (1000d * 1000d * 1000d);
        double moveDuration = distance / pixelsPerNanoSecond;

        double elapsedTime = now - getStartTime();

        elapsedTime = Math.min(moveDuration, elapsedTime);

        double factor = elapsedTime / moveDuration;
        double currentX = start.getX() + (xDelta * factor);
        double currentY = start.getY() + (yDelta * factor);

        Rectangle2D boundary = metrics.getAquaticBoundary();

        // the simple fish continues his unfinished path like a billiard ball when hitting a wall
        if (currentX < boundary.getMinX()) {
            currentX = boundary.getMinX() + Math.abs((boundary.getMinX() - currentX));
        }
        if (currentY < boundary.getMinY()) {
            currentY = boundary.getMinY() + Math.abs((boundary.getMinY() - currentY));
        }

        if (currentX > boundary.getMaxX()) {
            currentX = (2 * boundary.getMaxX()) - currentX;
        }
        if (currentY > boundary.getMaxY()) {
            currentY = (2 * boundary.getMaxY()) - currentY;
        }

        getFish().setX(currentX);
        getFish().setY(currentY);

        return factor >= 1;
    }

    public static SwimAnimation green() {
        return new SwimAnimation(Color.GREEN);
    }


    public static SwimAnimation yellow() {
        return new SwimAnimation(Color.YELLOW);
    }


    public static SwimAnimation grey() {
        return new SwimAnimation(Color.GREY);
    }
}
