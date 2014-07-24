package ch.nickthegreek.jenkins.fishtank.simplefish;

import javafx.scene.image.Image;

public class AliveSwimAnimation extends SwimAnimation {

    @Override
    protected Image getFishImage() {
        return FishImages.getAliveImage();
    }

    public static AliveSwimAnimation create() {
        return new AliveSwimAnimation();
    }

}
