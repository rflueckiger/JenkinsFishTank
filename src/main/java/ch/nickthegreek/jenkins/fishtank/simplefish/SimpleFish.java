package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.Fish;
import ch.nickthegreek.jenkins.fishtank.FishState;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

public class SimpleFish implements Fish {

    private final String name;

    private FishState state;
    private double x;
    private double y;

    private FishRenderer renderer;
    private Move currentMove;

    public SimpleFish(String name, FishState state) {
        this.name = name;

        setState(state);
    }

    @Override
    public void setState(FishState state) {
        if (state == null) {
            throw new IllegalArgumentException("state must not be null.");
        }
        if (!state.equals(this.state)) {
            this.state = state;

            // TODO: add more renderer and special state transition renderer
            if (FishState.ALIVE.equals(state)) {
                renderer = ALIVE_RENDERER;
            } else if (FishState.DEAD.equals(state)) {
                renderer = DEAD_RENDERER;
            } else {
                renderer = GHOST_RENDERER;
            }
        }
    }

    @Override
    public FishState getState() {
        return state;
    }

    @Override
    public void update(long now, Rectangle2D boundary) {
        // TODO: consider trajectory for nicer fish painting
        // TODO: consider move switch during single update step

        // 1. fish has no plan -> plan a new move
        if (currentMove == null) {
            currentMove = new Move(this, now);
        }

        // 2. fish has a plan -> calculate current position in move
        currentMove.moveFish(now, boundary);
        if (currentMove.isFinished()) {
            currentMove = null;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        // TODO: fish should look like fish :-)
        // TODO: keep fish name labels always visible, if possible
        // TODO: maybe only label fish in a special state (e.g. dead, dead_pending, sick, sick_pending)

        renderer.draw(gc);
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
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

}