package ch.nickthegreek.jenkins.fishtank;


import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public interface Fish {

    void update(long now, Rectangle2D boundary);

    void draw(GraphicsContext gc);

    FishState getState();

    void setState(FishState state);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    String getName();

}
