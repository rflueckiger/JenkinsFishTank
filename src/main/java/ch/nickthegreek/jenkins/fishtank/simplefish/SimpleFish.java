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

    private static final StateTransitions transitions = new StateTransitions();

    private final String name;
    private final Queue<Animation> queuedAnimations = new LinkedList<>();

    private FishState state;
    private double x;
    private double y;
    private double angle;
    private Animation currentAnimation;
    private boolean fishTracing = false;

    private int plane;

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
            Animation[] animations = transitions.nextAnimations(this.state, state);
            forceAnimations(animations);

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
        if (fishTracing) fishTracing(String.format("fish update @ %s - currentAnimation is %s", now, currentAnimation));

        if (currentAnimation.isFinished()) {
            if (queuedAnimations.isEmpty() && currentAnimation.isRepeating()) {
                if (fishTracing) fishTracing(String.format("fish update @ %s - animation finished, restarting current animation %s", now, currentAnimation));

                currentAnimation.init(this, now, metrics);
            } else {
                if (currentAnimation == null) {
                    throw new IllegalStateException("no fish animation available.");
                }

                currentAnimation = queuedAnimations.poll();

                if (fishTracing) fishTracing(String.format("fish update @ %s - assigning animation from queue %s", now, currentAnimation));

                currentAnimation.init(this, now, metrics);
            }
        } else if (!currentAnimation.isStarted()) {
            currentAnimation.init(this, now, metrics);
        }

        currentAnimation.update(now, metrics);
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (fishTracing) fishTracing("draw fish");

        currentAnimation.draw(gc);
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
    public void setAngle(double angle) {
        if (fishTracing) fishTracing(String.format("fish set angle to %s", angle));

        this.angle = angle;
    }

    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    public void setPlane(int plane) {
        if (fishTracing) fishTracing(String.format("fish set plane to %s", plane));

        this.plane = plane;
    }

    @Override
    public int getPlane() {
        return plane;
    }

    @Override
    public String getName() {
        return name;
    }

    protected void initFishTracing() {
        String tracerFishName = Config.getInstance().getTestTracerFish();
        if (tracerFishName != null && tracerFishName.equals(getName())) {
            fishTracing = true;
        }
    }

    protected void fishTracing(String message) {
        if (fishTracing) {
            System.out.println(String.format("%s: %s", getName(), message));
        }
    }

}