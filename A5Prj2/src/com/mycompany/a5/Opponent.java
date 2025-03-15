package com.mycompany.a5;

import com.codename1.ui.geom.Point2D;
import com.codename1.charts.util.ColorUtil;
import java.util.Random;

public abstract class Opponent extends GameObject implements IMove {
    private int health;
    private float speed;
    private float direction;
    private Random rand = new Random(); // Add Random instance as field

    public Opponent(int size, int color) {
        super(size, color);
        this.health = 100;
        this.direction = rand.nextFloat() * 360;
        this.speed = 5;
    }

    @Override
    public void move() {
        // Calculate movement based on direction and speed
        float deltaX = (float)(Math.cos(Math.toRadians(direction)) * speed);
        float deltaY = (float)(Math.sin(Math.toRadians(direction)) * speed);
        
        // Use translate instead of setLocation
        translate(deltaX, deltaY);
        
        // Rotate to face movement direction
        rotate(direction);

        // Randomly change direction occasionally
        if (rand.nextFloat() < 0.02f) {  // 2% chance per move
        	direction += (rand.nextFloat() * 30 - 15);  // Change by up to +/-15 degrees
        }
        
        notifyObservers();
    }

    // In toString method, update to use getLocation()
    @Override
    public String toString() {
        Point2D loc = new Point2D(getLocation().getX(), getLocation().getY());
        return "Opponent: loc=(" + loc.getX() + ", " + loc.getY() + ") size=" + getSize() +
               " health=" + health + 
               " color=[" + ColorUtil.red(getColor()) + "," + 
               ColorUtil.green(getColor()) + "," + ColorUtil.blue(getColor()) + "]";
    }
}