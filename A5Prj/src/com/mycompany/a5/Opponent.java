package com.mycompany.a5;

import com.codename1.ui.geom.Point;
import com.codename1.charts.util.ColorUtil;
import java.util.Random;

public abstract class Opponent extends GameObject implements IMove {
    private int health;
    private float moveStep = 5.0f;  // Define move step for opponent
    private int direction;         // Movement direction in degrees (0-360)
    private int speed;             // Speed of the opponent

    // Constructor to initialize opponent attributes
    public Opponent(int size, Point location, int color) {
        super(size, location, color);  // Calls GameObject constructor
        this.health = 100;             // Default health
        this.direction = new Random().nextInt(360); // Random initial direction
        this.speed = 5;                // Default speed
    }

    // Implement the `move()` method from IMove
    @Override
    public void move() {
        // General movement logic based on direction and speed
        double theta = Math.toRadians(90 - direction);  // Convert direction to radians
        float deltaX = (float) Math.cos(theta) * speed; // X movement
        float deltaY = (float) Math.sin(theta) * speed; // Y movement

        // Update the location
        Point oldLocation = getLocation();
        int newX = Math.round(oldLocation.getX() + deltaX);
        int newY = Math.round(oldLocation.getY() + deltaY);
        setLocation(new Point(newX, newY));

        System.out.println("Opponent moved to new location: (" + newX + ", " + newY + ")");
        notifyObservers();  // Notify observers of the new location
    }


	// Method to simulate a fight with another game object (e.g., Astronaut)
    public void fight(GameObject target) {
        if (target instanceof Astronaut) {
            Astronaut astronaut = (Astronaut) target;
            Random rand = new Random();
            int damage = rand.nextInt(20) + 1;  // Random damage between 1 and 20
            astronaut.takeDamage(damage);      // Apply damage to the astronaut
            System.out.println("Opponent attacked astronaut causing " + damage + " damage!");

            // Opponent also takes some damage in return
            int retaliateDamage = rand.nextInt(10) + 1;
            this.takeDamage(retaliateDamage);
        }
    }

    // Method to reduce health of the opponent
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            die();
        } else {
            System.out.println("Opponent took " + damage + " damage, remaining health: " + this.health);
        }
    }

    // Method to handle opponent's death
    private void die() {
        System.out.println("Opponent has died!");
        notifyObservers();  // Notify observers of the death
    }

    // Specific directional movement methods
    public void moveRight() {
        direction = 0; // Set direction to right
        move();
    }

    public void moveLeft() {
        direction = 180; // Set direction to left
        move();
    }

    public void moveUp() {
        direction = 90; // Set direction to up
        move();
    }

    public void moveDown() {
        direction = 270; // Set direction to down
        move();
    }

    // String representation of the opponent
    @Override
    public String toString() {
        Point loc = getLocation();  // Get the current location
        return "Opponent: loc=(" + loc.getX() + ", " + loc.getY() + ") size=" + getSize() +
               " health=" + health + 
               " color=[" + ColorUtil.red(getColor()) + "," + ColorUtil.green(getColor()) + "," + ColorUtil.blue(getColor()) + "]";
    }
}
