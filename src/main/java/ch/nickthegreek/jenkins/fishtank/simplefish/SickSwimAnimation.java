package ch.nickthegreek.jenkins.fishtank.simplefish;


import javafx.scene.image.Image;

public class SickSwimAnimation extends SwimAnimation {

    @Override
    protected Image getFishImage() {
        return FishImages.getSickImage();
    }

    public static SickSwimAnimation create() {
        return new SickSwimAnimation();
    }

}
