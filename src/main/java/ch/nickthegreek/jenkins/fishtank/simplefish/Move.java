package ch.nickthegreek.jenkins.fishtank.simplefish;

import javafx.geometry.Point2D;

import java.util.Random;

class Move {

    private final SimpleFish fish;
    private final long startTime;
    private final Point2D start;
    private final Point2D target;

    private final Random rnd = new Random();

    private boolean finished = false;

    public Move(SimpleFish fish, long startTime) {
        this.fish = fish;
        this.startTime = startTime;

        this.start = new Point2D(fish.getX(), fish.getY());

        // set a target location for the move
        // TODO: massively improve target location - add random element
        double xRange = rnd.nextGaussian() * 100 * (rnd.nextBoolean() ? 1 : -1);
        double yRange = rnd.nextGaussian() * 50 * (rnd.nextBoolean() ? 1 : -1);
        this.target = new Point2D(start.getX() + xRange, start.getY() + yRange);
    }

    public void moveFish(long now) {
        // TODO: add fish tracer for debugging (constant to define name of one fish for tracing)
        // TODO: fishes should not move outside of the boundaries
        // TODO: fish should have more interesting moves, e.g. dashes (velocity curves, etc.)
        double xDelta = target.getX() - start.getX();
        double yDelta = target.getY() - start.getY();
        double distance = Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
        double pixelsPerNanoSecond = 10d / (1000d * 1000d * 1000d);
        double moveDuration = distance / pixelsPerNanoSecond;

        double elapsedTime = now - startTime;

        elapsedTime = Math.min(moveDuration, elapsedTime);

        double factor = elapsedTime / moveDuration;
        double currentX = start.getX() + (xDelta * factor);
        double currentY = start.getY() + (yDelta * factor);

        fish.setX(currentX);
        fish.setY(currentY);

        if (factor >= 1) {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }
}