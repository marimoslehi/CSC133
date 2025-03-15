package com.mycompany.a5;

import com.codename1.ui.geom.Point2D;
import com.codename1.charts.util.ColorUtil;

public abstract class Rescuer extends GameObject implements IMove {
    private float moveStep = 10.0f; // Define move step for rescuer

    public Rescuer(int size, int color) {
        super(size, color);
    }

    // Method to rescue an astronaut
    public void rescue(Astronaut astronaut) {
        if (isNearby(astronaut)) {
            astronaut.rescue();
            notifyObservers();
        }
    }

    // Method to check if the astronaut is nearby for rescue
    private boolean isNearby(GameObject go) {
        Point2D myLocation = new Point2D(
            getLocation().getX(),
            getLocation().getY()
        );
        Point2D objectLocation = go.getLocation();
        float dx = (float)(myLocation.getX() - objectLocation.getX());
        float dy = (float)(myLocation.getY() - objectLocation.getY());
        float distanceSquared = dx * dx + dy * dy;
        return distanceSquared <= 10000;
    }

    // Movement methods using translate instead of setLocation
    public void moveRight() {
        translate(moveStep, 0);
    }

    public void moveLeft() {
        translate(-moveStep, 0);
    }

    public void moveUp() {
        translate(0, moveStep);
    }

    public void moveDown() {
        translate(0, -moveStep);
    }

    @Override
    public void move() {
        // General movement logic can be implemented here if needed
    }

    @Override
    public String toString() {
    	Point2D location = getLocation();
        return "Rescuer: loc=(" + location.getX() + ", " + location.getY() + 
               ") size=" + getSize() +
               " color=[" + ColorUtil.red(getColor()) + "," + 
               ColorUtil.green(getColor()) + "," + ColorUtil.blue(getColor()) + "]";
    }
}