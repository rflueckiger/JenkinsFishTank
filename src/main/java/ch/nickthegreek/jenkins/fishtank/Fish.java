package ch.nickthegreek.jenkins.fishtank;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Fish {

    private final String name;
    private FishRenderer renderer;
    private FishState state;
    private double x;
    private double y;

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
            if (FishState.ALIVE.equals(state)) {
                renderer = ALIVE_RENDERER;
            } else if (FishState.DEAD.equals(state)) {
                renderer = DEAD_RENDERER;
            } else {
                renderer = GHOST_RENDERER;
            }
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
        gc.fillOval(getX(), getY(), 10, 10);
        gc.fillText(getName(), getX(), getY());
    };

    FishRenderer DEAD_RENDERER = gc -> {
        gc.setFill(Color.RED);
        gc.fillOval(getX(), getY(), 10, 10);
        gc.fillText(getName(), getX(), getY());
    };

    FishRenderer GHOST_RENDERER = gc -> {
        gc.setFill(Color.GREY);
        gc.fillOval(getX(), getY(), 10, 10);
        gc.fillText(getName(), getX(), getY());
    };

}