package com.mycompany.a5;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.charts.util.ColorUtil;

public class Spaceship extends GameObject implements IDrawable {
    private int doorSize;  // The size of the spaceship's door
    private float moveStep = 10.0f;  // Step size for movement
    private static Spaceship spaceshipInstance;  // Singleton instance

    // Private constructor for the Singleton pattern
    Spaceship(int size, int color) {
        super(size, color);  // Updated constructor call
        this.doorSize = 100;  // Default door size
    }

    // Updated singleton method
    public static Spaceship getInstance(int size, int color) {
        if (spaceshipInstance == null) {
            spaceshipInstance = new Spaceship(size, color);
        }
        return spaceshipInstance;
    }

    public void expand() {
        if (doorSize < 1000) {
            doorSize += 10;
            System.out.println("Spaceship door expanded to: " + doorSize);
            notifyObservers();
        }
    }

    public void contract() {
        if (doorSize > 50) {
            doorSize -= 10;
            System.out.println("Spaceship door contracted to: " + doorSize);
            notifyObservers();
        }
    }

    // Updated movement methods to use translate
    public void moveRight() {
        translate(moveStep, 0);
        System.out.println("Spaceship moved right to: " + getLocation());
        notifyObservers();
    }

    public void moveLeft() {
        translate(-moveStep, 0);
        System.out.println("Spaceship moved left to: " + getLocation());
        notifyObservers();
    }

    public void moveUp() {
        translate(0, moveStep);
        System.out.println("Spaceship moved up to: " + getLocation());
        notifyObservers();
    }

    public void moveDown() {
        translate(0, -moveStep);
        System.out.println("Spaceship moved down to: " + getLocation());
        notifyObservers();
    }

    public boolean isInsideDoor(GameObject go) {
        Point2D spaceshipLocation = getLocation();
        Point2D objectLocation = go.getLocation();

        int halfDoorSize = doorSize / 2;

        return (objectLocation.getX() >= spaceshipLocation.getX() - halfDoorSize &&
                objectLocation.getX() <= spaceshipLocation.getX() + halfDoorSize &&
                objectLocation.getY() >= spaceshipLocation.getY() - halfDoorSize &&
                objectLocation.getY() <= spaceshipLocation.getY() + halfDoorSize);
    }

    @Override
    public String toString() {
        Point2D loc = getLocation();
        return "Spaceship: loc=(" + loc.getX() + ", " + loc.getY() + ") size=" + getSize() + 
               " doorSize=" + doorSize + 
               " color=[" + ColorUtil.red(getColor()) + "," + 
               ColorUtil.green(getColor()) + "," + ColorUtil.blue(getColor()) + "]";
    }

    public boolean isNear(GameObject obj) {
        Point2D myLoc = getLocation();
        Point2D objLoc = obj.getLocation();
        float dx = (float)(myLoc.getX() - objLoc.getX());
        float dy = (float)(myLoc.getY() - objLoc.getY());
        float distance = dx * dx + dy * dy;
        return distance <= 2500; // 50 * 50, for a radius of 50 units
    }

    public boolean contains(Point2D location) {
        Point2D center = getLocation();
        float radius = getSize() / 2.0f;
        float dx = (float)(center.getX() - location.getX());
        float dy = (float)(center.getY() - location.getY());
        return (dx * dx + dy * dy) <= (radius * radius);
    }
    
    @Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        Transform oldTransform = Transform.makeIdentity();
        g.getTransform(oldTransform);
        
        // Perform "local origin" transform - part two
        g.translate(pCmpRelPrnt.getX(), pCmpRelPrnt.getY());
        
        // Append local transform
        Transform localTransform = Transform.makeIdentity();
        localTransform.setTranslation((float)getLocation().getX(), (float)getLocation().getY());
        g.transform(localTransform);
        
        // Draw the spaceship
        g.setColor(getColor());
        int size = getSize();
        g.fillArc(-size/2, -size/2, size, size, 0, 360);
        
        // Restore graphics state
        g.setTransform(oldTransform);
    }

	public void takeDamage(int i) {
		// TODO Auto-generated method stub
		
	}

	public void moveTo(Point2D location) {
		// TODO Auto-generated method stub
		
	}

	public void expandDoor() {
		// TODO Auto-generated method stub
		
	}

	public void contractDoor() {
		// TODO Auto-generated method stub
		
	}
}