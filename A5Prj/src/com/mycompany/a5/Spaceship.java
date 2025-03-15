package com.mycompany.a5;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public class Spaceship extends GameObject implements IDrawable {
    private static Spaceship instance;
    private int doorSize; // Represents the size of the spaceship door
    private final int maxDoorSize = 100; // Maximum door size
    private final int minDoorSize = 10;  // Minimum door size
    private static final boolean soundOn = GameWorld.getInstance().isSoundOn();
    private static final Sound spaceshipDoorSound = GameWorld.getInstance().getSpaceshipDoorSound();
    private int visualSize; // Used for drawing the spaceship


    // Constructor
    Spaceship(int size, Point location, int color) {
        super(size, location, color);
        this.doorSize = 50; // Default door size
        this.visualSize = size; // Start with the same value as size
    }

    // Singleton pattern for Spaceship instance
    public static Spaceship getInstance(int size, Point location, int color) {
        if (instance == null) {
            instance = new Spaceship(size, location, color);
        }
        return instance;
    }

    // Corrected draw method
    @Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        if (getLocation() == null) {
            System.err.println("Error: Spaceship location is null.");
            return;
        }
        if (getSize() <= 0) {
            System.err.println("Error: Spaceship size is invalid.");
            return;
        }

        // Calculate position
        int x = (int) (getLocation().getX() - getSize() / 2 + pCmpRelPrnt.getX());
        int y = (int) (getLocation().getY() - getSize() / 2 + pCmpRelPrnt.getY());

        // Set color and draw the filled circle
        g.setColor(ColorUtil.BLACK); // Set the color to black
        g.fillArc(x, y, visualSize, visualSize, 0, 360); // Use visualSize for the rendering
        
     
    }

    @Override
    public String toString() {
        return "Spaceship: " + super.toString() + ", doorSize=" + doorSize;
    }

    // Expand the spaceship door
    public void expand() {
        if (doorSize < maxDoorSize) {
        	doorSize += 10; // Increase door size
            visualSize += 10; // Increase visual size for rendering
            System.out.println("Spaceship door expanded to: " + doorSize);
            if (soundOn) {
            	System.out.println("Playing expansion sound");
                spaceshipDoorSound.play();
            }
        } else {
            System.out.println("Spaceship door is already at maximum size.");
        }
    }

    // Contract the spaceship door
    public void contract() {
        if (doorSize > minDoorSize) {
            doorSize -= 10; // Decrease door size
            visualSize -= 10; // Decrease visual size for rendering
            System.out.println("Spaceship door contracted to: " + doorSize);
            if (soundOn) {
            	System.out.println("Playing contraction sound");
                spaceshipDoorSound.play();
            }
        } else {
            System.out.println("Spaceship door is already at minimum size.");
        }
    }

    // Move methods for the spaceship
    public void moveUp() {
        Point currentLocation = getLocation();
        setLocation(new Point(currentLocation.getX(), currentLocation.getY() - 10)); // Move up by 10 units
        System.out.println("Spaceship moved up to: " + getLocation());
    }

    public void moveDown() {
        Point currentLocation = getLocation();
        setLocation(new Point(currentLocation.getX(), currentLocation.getY() + 10)); // Move down by 10 units
        System.out.println("Spaceship moved down to: " + getLocation());
    }

    public void moveLeft() {
        Point currentLocation = getLocation();
        setLocation(new Point(currentLocation.getX() - 10, currentLocation.getY())); // Move left by 10 units
        System.out.println("Spaceship moved left to: " + getLocation());
    }

    public void moveRight() {
        Point currentLocation = getLocation();
        setLocation(new Point(currentLocation.getX() + 10, currentLocation.getY())); // Move right by 10 units
        System.out.println("Spaceship moved right to: " + getLocation());
    }

    public boolean isNear(GameObject obj) {
        // Ensure the object is valid
        if (obj == null) {
            return false;
        }

        // Calculate the distance between the spaceship and the object
        double distance = calculateDistance(this.getLocation(), obj.getLocation());

        // Define a proximity threshold (e.g., 50 units)
        return distance <= 50; // Replace 50 with your desired proximity threshold
    }

    // Helper method to calculate distance
    private double calculateDistance(Point p1, Point p2) {
        int dx = p2.getX() - p1.getX(); // Difference in X coordinates
        int dy = p2.getY() - p1.getY(); // Difference in Y coordinates
        return Math.sqrt(dx * dx + dy * dy); // Calculate Euclidean distance
    }

	public boolean contains(Point astronautLocation) {
    int shipX = (int) this.getLocation().getX();
    int shipY = (int) this.getLocation().getY();
    int halfSize = this.getSize() / 2;

    int astroX = (int) astronautLocation.getX();
    int astroY = (int) astronautLocation.getY();

    return astroX >= shipX - halfSize && astroX <= shipX + halfSize &&
           astroY >= shipY - halfSize && astroY <= shipY + halfSize;
}




}

