package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.Fish;
import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

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

    protected void rotate(GraphicsContext gc, double angle, double x, double y) {
        Rotate rotate = new Rotate(angle, x, y);
        gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(), rotate.getTy());
    }

    protected void drawRotatedText(GraphicsContext gc, String text, double angle, double x, double y) {
        gc.save();
        rotate(gc, angle, x, y);
        gc.fillText(text, x, y);
        gc.restore();
    }

    protected void drawRotatedImage(GraphicsContext gc, Image image, double angle, double x, double y) {
        gc.save();
        rotate(gc, angle, x + image.getWidth() / 2, y + image.getHeight() / 2);
        gc.drawImage(image, x, y);
        gc.restore();
    }

}