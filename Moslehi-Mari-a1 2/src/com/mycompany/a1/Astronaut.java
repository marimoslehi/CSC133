package com.mycompany.a1;

import java.util.Random;
import com.codename1.charts.models.Point;

public class Astronaut extends GameObject implements IMoving {
    private int health;  // Astronaut's health, ranges from 0 to 5
    private float speed;  // Astronaut's speed depends on health

    // Constructor to initialize astronaut attributes
    public Astronaut(int size, Point location, int color, int health) {
        super(size, location, color);  // Calls GameObject constructor
        this.health = health;
        updateSpeed();  // Initialize speed based on health
    }

    // Method to decrement the health of the astronaut
    public void decrementHealth() {
        if (health > 0) {
            health--;  // Decrease health by 1
            updateSpeed();  // Update speed based on new health value
        }
    }

    // Method to get the health value of the astronaut
    public int getHealth() {
        return health;
    }

    // Method to update speed based on health
    private void updateSpeed() {
        // Speed is directly proportional to health. Set to 0 if health is 0.
        this.speed = health > 0 ? health * 1.0f : 0;  
        // You could change the multiplier (currently 1.0f) to adjust the correlation
    }

    // Implement move behavior for astronaut
    @Override
    public void move() {
        // Ensure the astronaut only moves if speed > 0
        if (speed > 0) {
            Random rand = new Random();
            float deltaX = rand.nextFloat() * speed;  // Move by a random fraction of the speed
            float deltaY = rand.nextFloat() * speed;

            // Randomize movement direction for more variability (up/down, left/right)
            if (rand.nextBoolean()) {
                deltaX = -deltaX;  // Randomly flip direction on x-axis
            }
            if (rand.nextBoolean()) {
                deltaY = -deltaY;  // Randomly flip direction on y-axis
            }

            // Update location based on the calculated deltas
            Point oldLocation = getLocation();
            setLocation(new Point(oldLocation.getX() + deltaX, oldLocation.getY() + deltaY));
        }
    }

    @Override
    public String toString() {
        return "Astronaut: loc=" + getLocation() + " size=" + getSize() + " speed=" + speed + " health=" + health;
    }
}

