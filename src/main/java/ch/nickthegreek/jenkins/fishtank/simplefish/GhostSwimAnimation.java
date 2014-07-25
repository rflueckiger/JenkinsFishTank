package ch.nickthegreek.jenkins.fishtank.simplefish;

import javafx.scene.image.Image;


public class GhostSwimAnimation extends SwimAnimation {

    @Override
    protected Image getFishImageL() {
        return FishImages.getGhostImageL();
    }

    @Override
    protected Image getFishImageR() {
        return FishImages.getGhostImageR();
    }

    public static GhostSwimAnimation create() {
        return new GhostSwimAnimation();
    }

}
