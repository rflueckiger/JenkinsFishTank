package ch.nickthegreek.jenkins.fishtank;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

public class Fish {

    private Random rnd = new Random();
    private final String name;
    private FishRenderer renderer;
    private FishState state;
    private double x;
    private double y;
    private Move currentMove;

    public Fish(String name, FishState state) {
        this.name = name;

        setState(state);
    }

    public void setState(FishState state) {
        if (state == null) {
            throw new IllegalArgumentException("state must not be null.");
        }
        if (!state.equals(this.state)) {
            this.state = state;

            // TODO: add more renderer and special state transition renderer
            // TODO: fish should look like fish :-)
            if (FishState.ALIVE.equals(state)) {
                renderer = ALIVE_RENDERER;
            } else if (FishState.DEAD.equals(state)) {
                renderer = DEAD_RENDERER;
            } else {
                renderer = GHOST_RENDERER;
            }
        }
    }

    public void update(long now) {
        // TODO: consider trajectory for nicer fish painting
        // TODO: consider move switch during single update step

        // 1. fish has no plan -> plan a new move
        if (currentMove == null) {
            currentMove = new Move(this, now);
        }

        // 2. fish has a plan -> calculate current position in move
        currentMove.moveFish(now);
        if (currentMove.isFinished()) {
            currentMove = null;
        }
    }
    
    public void draw(GraphicsContext gc) {
        renderer.draw(gc);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    interface FishRenderer {
        void draw(GraphicsContext gc);
    }

    FishRenderer ALIVE_RENDERER = gc -> {
        gc.setFill(Color.GREEN);
        gc.setFont(Font.font(9));
        gc.fillOval(getX(), getY(), 10, 10);
        gc.fillText(getName(), getX(), getY());
    };

    FishRenderer DEAD_RENDERER = gc -> {
        gc.setFill(Color.RED);
        gc.setFont(Font.font(9));
        gc.fillOval(getX(), getY(), 10, 10);
        gc.fillText(getName(), getX(), getY());
    };

    FishRenderer GHOST_RENDERER = gc -> {
        gc.setFill(Color.GREY);
        gc.setFont(Font.font(9));
        gc.fillOval(getX(), getY(), 10, 10);
        gc.fillText(getName(), getX(), getY());
    };

    class Move {
        private final Fish fish;
        private final long startTime;
        private final Point2D start;
        private final Point2D target;

        private boolean finished = false;

        public Move(Fish fish, long startTime) {
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
    
}