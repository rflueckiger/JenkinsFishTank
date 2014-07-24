package ch.nickthegreek.jenkins.fishtank.simplefish;

import javafx.scene.image.Image;


public class GhostSwimAnimation extends SwimAnimation {

    @Override
    protected Image getFishImage() {
        return FishImages.getGhostImage();
    }

    public static GhostSwimAnimation create() {
        return new GhostSwimAnimation();
    }

}
