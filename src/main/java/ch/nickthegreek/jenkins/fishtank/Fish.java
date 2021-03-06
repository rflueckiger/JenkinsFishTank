package ch.nickthegreek.jenkins.fishtank;


import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public interface Fish {

    void update(long now, FishTankMetrics metrics);

    void draw(GraphicsContext gc);

    FishState getState();

    void setState(FishState state);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    String getName();

    void setAngle(double angle);

    double getAngle();

    void setPlane(int plane);

    int getPlane();
}
