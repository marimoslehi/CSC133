package com.mycompany.a5;

import java.util.Random;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;

public class Astronaut extends GameObject implements IDrawable, ISelectable {
	    private Random rand = new Random(); // Add this as a class field
	    private int health;
	    private float speed;
	    private float direction;
	    private boolean selected;
	    private boolean isRescued;
	    private final Random random = new Random();

    // Constructor
    public Astronaut(int size, Point location, int color, int health) {
    	 super(size, color);  // Call GameObject constructor without location
         this.health = health;
         this.speed = 2;  // Default speed
         Random rand = new Random();
         this.direction = random.nextFloat() * 360;  // Random initial direction
         this.selected = false;
         this.isRescued = false;
    }
    


	private int getRandomSpeed() {
        Random rand = new Random();
        return rand.nextInt(3) + 1; // Speed between 1 and 3 for slower movement
    }

    public void move() {
        if (!isRescued) {
            // Calculate movement based on direction and speed
            float deltaX = (float)(Math.cos(Math.toRadians(direction)) * speed);
            float deltaY = (float)(Math.sin(Math.toRadians(direction)) * speed);
            
            // Use translate instead of directly setting location
            translate(deltaX, deltaY);
            
            // Rotate to face movement direction
            rotate(direction);

            // Randomly change direction occasionally
            if (rand.nextFloat() < 0.02f) {  // 2% chance per move
            	direction += (rand.nextFloat() * 30 - 15);  // Change by up to +/-15 degrees
            }
        }
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
        // Save current transform
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        
        // Perform "local origin" transform - part two
        g.translate(pCmpRelPrnt.getX(), pCmpRelPrnt.getY());
        
        // Append local transform
        Transform localXform = getLocalTransform();
        g.transform(localXform);
        
        // Calculate triangle points in local space
        int size = getSize();
        int[] xPoints = {0, -size/2, size/2};
        int[] yPoints = {-size/2, size/2, size/2};
        
        // Set color and draw
        g.setColor(getColor());
        if (selected) {
            g.drawPolygon(xPoints, yPoints, 3); // Outline if selected
        } else {
            g.fillPolygon(xPoints, yPoints, 3); // Fill if not selected
        }
        
        // Restore original transform
        g.setTransform(gXform);
    }

    // Override toString
    @Override
    public String toString() {
        return "Astronaut: " + super.toString() + ", health=" + health + ", selected=" + selected;
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
    public float getSpeed() {
        return speed;
    }
    
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    

    @Override
    public boolean isSelected() {
        return selected;
    }
	
	
}
