package com.mycompany.a1;

import com.codename1.charts.models.Point;

public class Spaceship extends GameObject {
    private int doorSize;  // Additional attribute specific to Spaceship
    private float moveStep = 10.0f;  // Define a move step size

    // Constructor to initialize spaceship attributes
    public Spaceship(int size, Point location, int color) {
        super(size, location, color);  // Calls GameObject constructor
        this.doorSize = 100;  // Default door size
    }

    public void expand() {
        if (doorSize < 1000) doorSize += 10;
    }

    public void contract() {
        if (doorSize > 50) doorSize -= 10;
    }

    // Add movement methods
    public void moveRight() {
        Point oldLocation = getLocation();
        setLocation(new Point(oldLocation.getX() + moveStep, oldLocation.getY()));
    }

    public void moveLeft() {
        Point oldLocation = getLocation();
        setLocation(new Point(oldLocation.getX() - moveStep, oldLocation.getY()));
    }

    public void moveUp() {
        Point oldLocation = getLocation();
        setLocation(new Point(oldLocation.getX(), oldLocation.getY() + moveStep));
    }

    public void moveDown() {
        Point oldLocation = getLocation();
        setLocation(new Point(oldLocation.getX(), oldLocation.getY() - moveStep));
    }

    // Check if a game object is within the door of the spaceship
    public boolean isInsideDoor(GameObject go) {
        Point spaceshipLocation = getLocation();
        Point objectLocation = go.getLocation();

        // Calculate half of the door size
        float halfDoorSize = doorSize / 2.0f;

        // Check if the object is within the spaceship's door area
        if (objectLocation.getX() >= spaceshipLocation.getX() - halfDoorSize &&
            objectLocation.getX() <= spaceshipLocation.getX() + halfDoorSize &&
            objectLocation.getY() >= spaceshipLocation.getY() - halfDoorSize &&
            objectLocation.getY() <= spaceshipLocation.getY() + halfDoorSize) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Spaceship: loc=" + getLocation() + " size=" + doorSize;
    }
}
