package ch.nickthegreek.jenkins.fishtank.simplefish;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

abstract class Animation {

    private boolean started = false;
    private boolean finished = false;
    private boolean repeating = false;

    public Animation(boolean repeating) {
        this.repeating = repeating;
    }

    public void start(long startTime) {
        finished = false;

        doInit(startTime);

        started = true;
    }

    protected abstract void doInit(long startTime);

    public void update(long now, Rectangle2D boundary) {
        if (!isStarted()) {
            throw new IllegalStateException("animation must be started first.");
        }

        finished = doUpdate(now, boundary);
    }

    protected abstract boolean doUpdate(long now, Rectangle2D boundary);

    public void draw(GraphicsContext gc) {
        doDraw(gc);
    }

    protected abstract void doDraw(GraphicsContext gc);

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isRepeating() {
        return repeating;
    }
}