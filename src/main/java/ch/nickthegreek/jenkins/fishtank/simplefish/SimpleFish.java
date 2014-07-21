package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.Config;
import ch.nickthegreek.jenkins.fishtank.Fish;
import ch.nickthegreek.jenkins.fishtank.FishState;
import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static ch.nickthegreek.jenkins.fishtank.FishState.*;

public class SimpleFish implements Fish {

    private final String name;
    private final Queue<Animation> queuedAnimations = new LinkedList<>();

    private FishState state;
    private double x;
    private double y;
    private Animation currentAnimation;
    private boolean fishTracing = false;

    private List<FishState> labelStates = Arrays.asList(
            DEAD, DEAD_PENDING,
            SICK, SICK_PENDING,
            ALIVE_PENDING);

    public SimpleFish(String name, FishState state) {
        this.name = name;

        initFishTracing();
        if (fishTracing) fishTracing(String.format("fish created in state %s", state));

        setState(state);
    }

    @Override
    public void setState(FishState state) {
        if (state == null) {
            throw new IllegalArgumentException("state must not be null.");
        }
        if (!state.equals(this.state)) {
            // TODO: add dive animation for resurrected fishes
            // TODO: add sick animation for sick fishes
            // TODO: add pulsating animation for "pending" fishes
            // TODO: add alternate animation for ghost fishes (slower?)
            // TODO: add more fish drawing variety (e.g. alive fishes in different green/blue shades)
            // TODO: extract animation mapping to separate "configuration-like" class!

            if (this.state == null) {
                if (state.equals(ALIVE))                { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(ALIVE_PENDING))   { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(SICK))            { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(SICK_PENDING))    { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(DEAD))            { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(DEAD_PENDING))    { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(GHOST))           { forceAnimations(new SwimAnimation(true, this, Color.GREY)); }
                else { throw new IllegalArgumentException(String.format("unknown target state: %s", state)); }
            } else if (this.state.equals(ALIVE)) {
                if (state.equals(ALIVE_PENDING))        { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(SICK))            { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(SICK_PENDING))    { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(DEAD))            { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(DEAD_PENDING))    { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(GHOST))           { forceAnimations(new SwimAnimation(true, this, Color.GREY)); }
                else { throw new IllegalArgumentException(String.format("unknown target state: %s", state)); }
            } else if (this.state.equals(ALIVE_PENDING)) {
                if (state.equals(ALIVE))                { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(SICK))            { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(SICK_PENDING))    { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(DEAD))            { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(DEAD_PENDING))    { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(GHOST))           { forceAnimations(new SwimAnimation(true, this, Color.GREY)); }
                else { throw new IllegalArgumentException(String.format("unknown target state: %s", state)); }
            } else if (this.state.equals(SICK)) {
                if (state.equals(ALIVE))                { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(ALIVE_PENDING))   { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(SICK_PENDING))    { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(DEAD))            { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(DEAD_PENDING))    { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(GHOST))           { forceAnimations(new SwimAnimation(true, this, Color.GREY)); }
                else { throw new IllegalArgumentException(String.format("unknown target state: %s", state)); }
            } else if (this.state.equals(SICK_PENDING)) {
                if (state.equals(ALIVE))                { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(ALIVE_PENDING))   { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(SICK))            { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(DEAD))            { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(DEAD_PENDING))    { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(GHOST))           { forceAnimations(new SwimAnimation(true, this, Color.GREY)); }
                else { throw new IllegalArgumentException(String.format("unknown target state: %s", state)); }
            } else if (this.state.equals(DEAD)) {
                if (state.equals(ALIVE))                { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(ALIVE_PENDING))   { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(SICK))            { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(SICK_PENDING))    { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(DEAD_PENDING))    { forceAnimations(new FloatAnimation(this, Color.RED)); }
                else if (state.equals(GHOST))           { forceAnimations(new SwimAnimation(true, this, Color.GREY)); }
                else { throw new IllegalArgumentException(String.format("unknown target state: %s", state)); }
            } else if (this.state.equals(DEAD_PENDING)) {
                if (state.equals(ALIVE))                { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(ALIVE_PENDING))   { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(SICK))            { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(SICK_PENDING))    { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(DEAD))            { forceAnimations(new FloatAnimation(this, Color.RED)); }
                else if (state.equals(GHOST))           { forceAnimations(new SwimAnimation(true, this, Color.GREY)); }
                else { throw new IllegalArgumentException(String.format("unknown target state: %s", state)); }
            } else if (this.state.equals(GHOST)) {
                if (state.equals(ALIVE))                { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(ALIVE_PENDING))   { forceAnimations(new SwimAnimation(true, this, Color.GREEN)); }
                else if (state.equals(SICK))            { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(SICK_PENDING))    { forceAnimations(new SwimAnimation(true, this, Color.YELLOW)); }
                else if (state.equals(DEAD))            { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else if (state.equals(DEAD_PENDING))    { forceAnimations(new SurfaceAnimation(this, Color.RED), new FloatAnimation(this, Color.RED)); }
                else { throw new IllegalArgumentException(String.format("unknown target state: %s", state)); }
            } else {
                throw new IllegalStateException(String.format("unknown current state: %s", this.state));
            }

            if (fishTracing) fishTracing(String.format("fish state changed from: %s to %s", this.state, state));
            if (fishTracing) fishTracing(String.format("queuedAnimations: %s", queuedAnimations.size()));

            this.state = state;
        }
    }

    private void forceAnimations(Animation... animations) {
        queuedAnimations.clear();
        for (Animation animation : animations) {
            queuedAnimations.add(animation);
        }

        if (fishTracing) fishTracing(String.format("fish animation forced - assigning animation from queue %s", currentAnimation));

        currentAnimation = queuedAnimations.poll();
    }

    @Override
    public FishState getState() {
        return state;
    }

    @Override
    public void update(long now, FishTankMetrics metrics) {
        // TODO: consider trajectory for nicer fish painting

        if (fishTracing) fishTracing(String.format("fish update @ %s - currentAnimation is %s", now, currentAnimation));

        if (currentAnimation.isFinished()) {
            if (queuedAnimations.isEmpty() && currentAnimation.isRepeating()) {
                if (fishTracing) fishTracing(String.format("fish update @ %s - animation finished, restarting current animation %s", now, currentAnimation));

                currentAnimation.start(now, metrics);
            } else {
                if (currentAnimation == null) {
                    throw new IllegalStateException("no fish animation available.");
                }

                currentAnimation = queuedAnimations.poll();

                if (fishTracing) fishTracing(String.format("fish update @ %s - assigning animation from queue %s", now, currentAnimation));

                currentAnimation.start(now, metrics);
            }
        } else if (!currentAnimation.isStarted()) {
            currentAnimation.start(now, metrics);
        }

        currentAnimation.update(now, metrics);
    }

    @Override
    public void draw(GraphicsContext gc) {
        // TODO: fish should look like fish :-)
        // TODO: keep fish name labels always visible, if possible
        // TODO: labels of dead fish should be drawn vertical or at least with an angle to prevent cluttering at the surface

        if (fishTracing) fishTracing("draw fish");

        currentAnimation.draw(gc);

        if (labelStates.contains(getState())) {
            drawLabel(gc);
        }
    }

    @Override
    public void setX(double x) {
        if (fishTracing) fishTracing(String.format("fish set x to %s", x));

        this.x = x;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setY(double y) {
        if (fishTracing) fishTracing(String.format("fish set y to %s", y));

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

    private void drawLabel(GraphicsContext gc) {
        gc.setFill(Color.GREY);
        gc.setFont(Font.font(9));
        gc.fillText(getName(), getX(), getY());
    }

    protected void initFishTracing() {
        String tracerFishName = Config.getInstance().getTestTracerFish();
        if (tracerFishName != null && tracerFishName.equals(getName())) {
            fishTracing = true;
        }
    }

    protected void fishTracing(String message) {
        if (fishTracing) {
            // TODO: replace with proper logging statement - slf4j or some such thing
            System.out.println(String.format("%s: %s", getName(), message));
        }
    }

}