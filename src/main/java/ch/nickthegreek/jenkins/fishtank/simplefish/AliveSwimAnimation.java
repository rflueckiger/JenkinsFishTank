package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishTankMetrics;
import javafx.scene.image.Image;

public class AliveSwimAnimation extends SwimAnimation {

    @Override
    protected void doInit(long startTime, FishTankMetrics metrics) {
        super.doInit(startTime, metrics);

        setLabelEnabled(false);
    }

    @Override
    protected Image getFishImageL() {
        return FishImages.getAliveImageL();
    }

    @Override
    protected Image getFishImageR() {
        return FishImages.getAliveImageR();
    }

    public static AliveSwimAnimation create() {
        return new AliveSwimAnimation();
    }

}
