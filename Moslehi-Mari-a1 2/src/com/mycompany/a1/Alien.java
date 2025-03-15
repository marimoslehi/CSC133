package com.mycompany.a1;

import com.codename1.charts.models.Point;
import java.util.Random;

public class Alien extends GameObject implements IMoving {
    private int speed;
    private int direction;  // Compass direction (in degrees)

    // Constructor to initialize alien attributes
    public Alien(int size, Point location, int color) {
        super(size, location, color);  // Calls GameObject constructor
        this.speed = 5;  // Fixed speed for Alien
        this.direction = new Random().nextInt(360);  // Random initial direction
    }

    // Method to implement movement behavior
    @Override
    public void move() {
        // Movement logic based on direction and speed
        double theta = Math.toRadians(90 - direction);  // Convert direction to radians
        float deltaX = (float) Math.cos(theta) * speed;
        float deltaY = (float) Math.sin(theta) * speed;

        // Update the object's location
        Point oldLocation = getLocation();
        setLocation(new Point(oldLocation.getX() + deltaX, oldLocation.getY() + deltaY));
    }

    @Override
    public String toString() {
        return "Alien: loc=" + getLocation() + " size=" + getSize() + " speed=" + speed + " direction=" + direction;
    }
}

