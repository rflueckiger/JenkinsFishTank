package ch.nickthegreek.jenkins.fishtank.simplefish;


import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static ch.nickthegreek.jenkins.fishtank.FishState.DEAD;
import static ch.nickthegreek.jenkins.fishtank.FishState.DEAD_PENDING;

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
