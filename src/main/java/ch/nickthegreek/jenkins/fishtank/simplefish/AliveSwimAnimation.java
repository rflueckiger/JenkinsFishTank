package ch.nickthegreek.jenkins.fishtank.simplefish;

import javafx.scene.image.Image;

public class AliveSwimAnimation extends SwimAnimation {

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
