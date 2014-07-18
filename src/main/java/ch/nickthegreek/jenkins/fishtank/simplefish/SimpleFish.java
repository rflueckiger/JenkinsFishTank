package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.Config;
import ch.nickthegreek.jenkins.fishtank.Fish;
import ch.nickthegreek.jenkins.fishtank.FishState;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SimpleFish implements Fish {

    private final String name;
    private final Queue<Animation> queuedAnimations = new LinkedList<>();

    private FishState state;
    private double x;
    private double y;
    private Animation currentAnimation;
    private boolean fishTracing = false;

    private List<FishState> labelStates = Arrays.asList(
            FishState.DEAD, FishState.DEAD_PENDING,
            FishState.SICK, FishState.SICK_PENDING,
            FishState.ALIVE_PENDING);

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
            // TODO: add more renderer and special state transition renderer

            if (FishState.ALIVE.equals(state) || FishState.ALIVE_PENDING.equals(state)) {
                if (this.state != null && (this.state.equals(FishState.DEAD) || this.state.equals(FishState.DEAD_PENDING))) {
                    queuedAnimations.clear();
//                    queuedAnimations.add(new DiveAnimation(this, Color.GREEN));
                    queuedAnimations.add(new SwimAnimation(true, this, Color.GREEN));
                } else {
                    queuedAnimations.clear();
                    queuedAnimations.add(new SwimAnimation(true, this, Color.GREEN));
                }
            } else if (FishState.DEAD.equals(state) || FishState.DEAD_PENDING.equals(state)) {
                if (this.state != null && (this.state.equals(FishState.ALIVE) || this.state.equals(FishState.ALIVE_PENDING)
                        || this.state.equals(FishState.SICK) || this.state.equals(FishState.SICK_PENDING)
                        || this.state.equals(FishState.GHOST))) {
                    queuedAnimations.clear();
//                    queuedAnimations.add(new SurfaceAnimation(this, Color.RED));
//                    queuedAnimations.add(new FloatAnimation(this, true, Color.RED));
                    queuedAnimations.add(new SwimAnimation(true, this, Color.RED));
                } else {
                    queuedAnimations.clear();
//                    queuedAnimations.add(new FloatAnimation(this, true, Color.RED));
                    queuedAnimations.add(new SwimAnimation(true, this, Color.RED));
                }
            } else if (FishState.SICK.equals(state) || FishState.SICK_PENDING.equals(state)) {
                if (this.state != null && (this.state.equals(FishState.DEAD) || this.state.equals(FishState.DEAD_PENDING))) {
                    queuedAnimations.clear();
//                    queuedAnimations.add(new DiveAnimation(this, Color.YELLOW));
                    queuedAnimations.add(new SwimAnimation(true, this, Color.YELLOW));
                } else {
                    queuedAnimations.clear();
                    queuedAnimations.add(new SwimAnimation(true, this, Color.YELLOW));
                }
            } else {
                queuedAnimations.clear();
                queuedAnimations.add(new SwimAnimation(true, this, Color.GREY));
            }

            if (fishTracing) fishTracing(String.format("fish state changed from: %s to %s", this.state, state));
            if (fishTracing) fishTracing(String.format("queuedAnimations: %s", queuedAnimations.size()));

            this.state = state;
        }
    }

    @Override
    public FishState getState() {
        return state;
    }

    @Override
    public void update(long now, Rectangle2D boundary) {
        // TODO: consider trajectory for nicer fish painting
        // TODO: animations currently can't be interrupted... which would ne nice!

        if (fishTracing) fishTracing(String.format("fish update @ %s - currentAnimation is %s", now, currentAnimation));

        if (currentAnimation == null) {
            currentAnimation = queuedAnimations.poll();

            if (fishTracing) fishTracing(String.format("fish update @ %s - assigning animation from queue %s", now, currentAnimation));

            currentAnimation.start(now);
        } else if (currentAnimation.isFinished()) {
            if (queuedAnimations.isEmpty() && currentAnimation.isRepeating()) {
                if (fishTracing) fishTracing(String.format("fish update @ %s - animation finished, restarting current animation %s", now, currentAnimation));

                currentAnimation.start(now);
            } else {
                // FIXME: if state transitions are not perfectly configured, currentAnimation could be null here!
                currentAnimation = queuedAnimations.poll();

                if (fishTracing) fishTracing(String.format("fish update @ %s - assigning animation from queue %s", now, currentAnimation));

                currentAnimation.start(now);
            }
        }

        currentAnimation.update(now, boundary);
    }

    @Override
    public void draw(GraphicsContext gc) {
        // TODO: fish should look like fish :-)
        // TODO: keep fish name labels always visible, if possible

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