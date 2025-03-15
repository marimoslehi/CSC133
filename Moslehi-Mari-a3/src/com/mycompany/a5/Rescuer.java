package com.mycompany.a5;

import com.codename1.ui.geom.Point;
import com.codename1.charts.util.ColorUtil;

public abstract class Rescuer extends GameObject implements IMove {
    private float moveStep = 10.0f; // Define move step for rescuer

    public Rescuer(int size, Point location, int color) {
        super(size, location, color); // Call GameObject constructor
    }

    // Method to rescue an astronaut
    public void rescue(Astronaut astronaut) {
        if (isNearby(astronaut)) {
            astronaut.rescue(); // Mark the astronaut as rescued
            System.out.println("Rescuer has rescued the astronaut at location: " + astronaut.getLocation());
            notifyObservers(); // Notify observers of the rescue action
        }
    }

    // Method to check if the astronaut is nearby for rescue
    private boolean isNearby(GameObject go) {
        Point rescuerLocation = getLocation();
        Point objectLocation = go.getLocation();

        int dx = rescuerLocation.getX() - objectLocation.getX();
        int dy = rescuerLocation.getY() - objectLocation.getY();
        int distanceSquared = dx * dx + dy * dy;

        // Define a proximity threshold for rescue (e.g., within 100 units)
        return distanceSquared <= 10000; // 100 * 100 for squared distance
    }

    // Movement methods for the rescuer
    public void moveRight() {
        Point oldLocation = getLocation();
        int newX = oldLocation.getX() + Math.round(moveStep);
        setLocation(new Point(newX, oldLocation.getY()));
        System.out.println("Rescuer moved right to: " + getLocation());
    }

    public void moveLeft() {
        Point oldLocation = getLocation();
        int newX = oldLocation.getX() - Math.round(moveStep);
        setLocation(new Point(newX, oldLocation.getY()));
        System.out.println("Rescuer moved left to: " + getLocation());
    }

    public void moveUp() {
        Point oldLocation = getLocation();
        int newY = oldLocation.getY() + Math.round(moveStep);
        setLocation(new Point(oldLocation.getX(), newY));
        System.out.println("Rescuer moved up to: " + getLocation());
    }

    public void moveDown() {
        Point oldLocation = getLocation();
        int newY = oldLocation.getY() - Math.round(moveStep);
        setLocation(new Point(oldLocation.getX(), newY));
        System.out.println("Rescuer moved down to: " + getLocation());
    }

    @Override
    public void move() {
        // General movement logic can be implemented here if needed
    }

    @Override
    public String toString() {
        Point loc = getLocation();
        return "Rescuer: loc=(" + loc.getX() + ", " + loc.getY() + ") size=" + getSize() +
               " color=[" + ColorUtil.red(getColor()) + "," + ColorUtil.green(getColor()) + "," + ColorUtil.blue(getColor()) + "]";
    }
}
