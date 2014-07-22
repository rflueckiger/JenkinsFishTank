package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.Fish;
import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

abstract class Animation {

    private boolean started = false;
    private boolean finished = false;
    private boolean repeating = false;

    private Fish fish;
    private long startTime;

    public Animation(boolean repeating) {
        this.repeating = repeating;
    }

    public void init(Fish fish, long startTime, FishTankMetrics metrics) {
        this.fish = fish;

        finished = false;

        this.startTime = startTime;
        doInit(startTime, metrics);

        started = true;
    }

    protected void doInit(long startTime, FishTankMetrics metrics) {
    }

    public void update(long now, FishTankMetrics metrics) {
        if (!isStarted()) {
            throw new IllegalStateException("animation must be started first.");
        }

        finished = doUpdate(now, metrics);
    }

    protected abstract boolean doUpdate(long now, FishTankMetrics metrics);

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

    public long getStartTime() {
        return startTime;
    }

    protected Fish getFish() {
        return fish;
    }

}