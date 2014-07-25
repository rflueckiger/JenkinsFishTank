package ch.nickthegreek.jenkins.fishtank.simplefish;


import javafx.scene.image.Image;

public class SickSwimAnimation extends SwimAnimation {

    @Override
    protected Image getFishImageL() {
        return FishImages.getSickImageL();
    }

    @Override
    protected Image getFishImageR() {
        return FishImages.getSickImageR();
    }

    public static SickSwimAnimation create() {
        return new SickSwimAnimation();
    }

}
