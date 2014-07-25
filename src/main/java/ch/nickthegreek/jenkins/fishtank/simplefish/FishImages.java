package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishState;
import javafx.scene.image.Image;

public class FishImages {

    private static Image alive_l = new Image("fishes/fish-alive-l.png");
    private static Image alive_r = new Image("fishes/fish-alive-r.png");
    private static Image dead_l = new Image("fishes/fish-dead-l.png");
    private static Image dead_r = new Image("fishes/fish-dead-r.png");
    private static Image sick_l = new Image("fishes/fish-sick-l.png");
    private static Image sick_r = new Image("fishes/fish-sick-r.png");
    private static Image ghost_l = new Image("fishes/fish-ghost-l.png");
    private static Image ghost_r = new Image("fishes/fish-ghost-r.png");

    public static Image getAliveImageL() { return alive_l; }
    public static Image getAliveImageR() {
        return alive_r;
    }

    public static Image getDeadImageL() {
        return dead_l;
    }
    public static Image getDeadImageR() {
        return dead_r;
    }

    public static Image getSickImageL() {
        return sick_l;
    }
    public static Image getSickImageR() {
        return sick_r;
    }

    public static Image getGhostImageL() {
        return ghost_l;
    }
    public static Image getGhostImageR() {
        return ghost_r;
    }
}
