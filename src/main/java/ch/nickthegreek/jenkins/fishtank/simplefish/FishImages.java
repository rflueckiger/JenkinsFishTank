package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishState;
import javafx.scene.image.Image;

public class FishImages {

    private static Image alive = new Image("fish-alive.png");
    private static Image dead = new Image("fish-dead.png");
    private static Image sick = new Image("fish-sick.png");
    private static Image ghost = new Image("fish-ghost.png");

    public static Image getAliveImage() {
        return alive;
    }

    public static Image getDeadImage() {
        return dead;
    }

    public static Image getSickImage() {
        return sick;
    }

    public static Image getGhostImage() {
        return ghost;
    }
}
