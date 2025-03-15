package com.mycompany.a5;

import java.util.Random;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public class Alien extends GameObject implements IDrawable {
    private int health;
    private int dx, dy; // Change in position per tick

    public Alien(int size, Point location, int color) {
        super(size, location, color);
        this.health = 100;
        this.dx = getRandomSpeed();
        this.dy = getRandomSpeed();
    }

    // Generate a random speed for movement
    private int getRandomSpeed() {
        Random rand = new Random();
        return rand.nextInt(5) + 1; // Speed between 1 and 5
    }
    
    // Move the alien
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
    
    // Reduce health
    public void takeDamage(int damage) {
        health = Math.max(health - damage, 0);
    }

    // Draw a filled square for the alien
    @Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        int x = (int) (getLocation().getX() - getSize() / 2 + pCmpRelPrnt.getX());
        int y = (int) (getLocation().getY() - getSize() / 2 + pCmpRelPrnt.getY());
        g.setColor(getColor());
        g.fillRect(x, y, getSize(), getSize());
    }

    public void fight(Astronaut astronaut) {
        // Reduce the health of the astronaut
        int damageToAstronaut = 20; // Define how much damage the astronaut takes
        astronaut.takeDamage(damageToAstronaut);
        astronaut.fadeColor(); // Change astronaut's color to represent damage

        // Reduce the health of the alien
        int damageToAlien = 10; // Define how much damage the alien takes
        this.takeDamage(damageToAlien);

        // Log the fight details
        System.out.println("Fight occurred between Alien and Astronaut.");
        System.out.println("Astronaut's remaining health: " + astronaut.getHealth());
        System.out.println("Alien's remaining health: " + this.health);

        // Optionally check if either object is "dead" (health <= 0)
        if (astronaut.getHealth() <= 0) {
            System.out.println("Astronaut has been defeated!");
            // Logic to remove the astronaut from the game world (if needed)
            GameWorld.getInstance().removeGameObject(astronaut);
        }

        if (this.health <= 0) {
            System.out.println("Alien has been defeated!");
            // Logic to remove the alien from the game world (if needed)
            GameWorld.getInstance().removeGameObject(this);
        }
    }

	public void fight() {
		// TODO Auto-generated method stub
		
	}

}


