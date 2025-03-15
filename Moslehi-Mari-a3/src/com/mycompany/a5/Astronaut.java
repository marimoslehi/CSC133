package com.mycompany.a5;

import java.util.Random;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public class Astronaut extends GameObject implements IDrawable, ISelectable {
    private int health;
    private boolean isSelected;
    private int dx, dy; // Change in position per tick
    private int speed; 

    // Constructor
    public Astronaut(int size, Point location, int color, int health) {
        super(size, location, color);
        this.health = health;
        this.isSelected = false;
        this.dx = getRandomSpeed();
        this.dy = getRandomSpeed();
    }
    
    private int getRandomSpeed() {
        Random rand = new Random();
        return rand.nextInt(3) + 1; // Speed between 1 and 3 for slower movement
    }

    // Move the astronaut
    public void move() {
        Point location = getLocation();
        location.setX(location.getX() + dx);
        location.setY(location.getY() + dy);

        // Bounce back if hitting boundaries
        if (location.getX() < 0 || location.getX() > 1000) {
            dx = -dx;
        }
        if (location.getY() < 0 || location.getY() > 1000) {
            dy = -dy;
        }

        setLocation(location);
    }


    // Health-related methods
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, health); // Ensure health doesn't drop below 0
    }

    public void takeDamage(int damage) {
        setHealth(health - damage);
        fadeColor();
        System.out.println("Astronaut took " + damage + " damage. Current health: " + health);
    }

    public void heal() {
        this.health = 100; // Reset health to full
        setColor(ColorUtil.GREEN); // Set color to green when healed
        System.out.println("Astronaut healed to full health.");
    }

    public void fadeColor() {
        int red = ColorUtil.red(getColor());
        int green = ColorUtil.green(getColor());
        int blue = ColorUtil.blue(getColor());
        setColor(ColorUtil.rgb(Math.max(red - 20, 0), Math.max(green - 20, 0), Math.max(blue - 20, 0)));
    }

    // Methods for ISelectable
    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean contains(int x, int y) {
        int objX = (int) getLocation().getX();
        int objY = (int) getLocation().getY();
        int halfSize = getSize() / 2;

        return (x >= objX - halfSize && x <= objX + halfSize && y >= objY - halfSize && y <= objY + halfSize);
    }

    // Methods for IDrawable
    @Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        int x = (int) (getLocation().getX() + pCmpRelPrnt.getX());
        int y = (int) (getLocation().getY() + pCmpRelPrnt.getY());
        int size = getSize();

        int[] xPoints = {x, x - size / 2, x + size / 2};
        int[] yPoints = {y, y + size, y + size};

        g.setColor(getColor());
        if (isSelected) {
            g.drawPolygon(xPoints, yPoints, 3); // Outline the triangle if selected
        } else {
            g.fillPolygon(xPoints, yPoints, 3); // Fill the triangle if not selected
        }
    }

    // Override toString
    @Override
    public String toString() {
        return "Astronaut: " + super.toString() + ", health=" + health + ", selected=" + isSelected;
    }

    public void decreaseHealth() {
        int damage = 10; // Define the amount of health reduction
        this.health = Math.max(this.health - damage, 0); // Reduce health but ensure it doesn't go below 0
        fadeColor(); // Update the astronaut's color to indicate damage
        reduceSpeed();
        System.out.println("Astronaut health decreased by " + damage + ". Current health: " + health);
    }
    


    public void reduceSpeed() {
    	this.speed = Math.max(1, (int) (5 * ((double) health / 100))); // Speed scales with health
        System.out.println("Astronaut's speed reduced (placeholder implementation).");
    }

    public void rescue() {
    	this.health = 100; // Reset health to full
        this.speed = 5; // Reset speed to normal
        setColor(ColorUtil.GREEN); // Indicate the astronaut has been rescued
        System.out.println("Astronaut rescued. Health and speed reset.");
    }
    
    // Getter for speed
    public int getSpeed() {
        return speed;
    }
	
	
}
