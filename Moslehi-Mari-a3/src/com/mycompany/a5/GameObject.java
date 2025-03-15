package com.mycompany.a5;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.Graphics;
import java.util.ArrayList;
import com.codename1.charts.util.ColorUtil;

public abstract class GameObject implements ICollider{
    private int size;
    private Point location;
    private int color;
    private ArrayList<IObserver> observers; // List of observers
    private ArrayList<GameObject> collisionList; // List to track handled collisions

    public GameObject(int size, Point location, int color) {
        this.size = size;
        this.location = location;
        this.color = color;
        observers = new ArrayList<>(); // Initialize observers list
        collisionList = new ArrayList<>(); // Initialize collision list
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
        notifyObservers(); // Notify observers of the change
    }

    public int getSize() {
        return size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyObservers(); // Notify observers of the change
    }

    @Override
    public String toString() {
        return "GameObject: loc=(" + location.getX() + ", " + location.getY() + 
               "), size=" + size + 
               ", color=[" + ColorUtil.red(color) + "," + ColorUtil.green(color) + "," + ColorUtil.blue(color) + "]";
    }
    
   // Observer management methods
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update(this); // Notify each observer
        }
    }

    public abstract void draw(Graphics g, Point pCmpRelPrnt);
    
 // Collision detection using bounding circle
    @Override
    public boolean collidesWith(GameObject otherObject) {
        double dx = this.location.getX() - otherObject.getLocation().getX();
        double dy = this.location.getY() - otherObject.getLocation().getY();
        double distanceSquared = dx * dx + dy * dy;

        double radiusSum = (this.size / 2.0) + (otherObject.getSize() / 2.0);
        return distanceSquared <= radiusSum * radiusSum;
    }

    @Override
    public void handleCollision(GameObject otherObject) {
        if (!collisionList.contains(otherObject)) {
            collisionList.add(otherObject);
            if (otherObject instanceof Alien && this instanceof Alien) {
                System.out.println("Alien collided with Alien! Creating a new Alien.");
                // Code to create and add a new Alien to the game world
            } else if (otherObject instanceof Alien && this instanceof Astronaut) {
                System.out.println("Alien collided with Astronaut! Reducing health and speed.");
                Astronaut astronaut = (Astronaut) this;
                astronaut.decreaseHealth();
                astronaut.fadeColor();
                astronaut.reduceSpeed();
            }
            // Additional collision logic can be added here
        }
    }

    // Clear the collision list for a new frame
    public void clearCollisions() {
        collisionList.clear();
    }

	public void setSelected(boolean isSelected) {
		// TODO Auto-generated method stub
		
	}

	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(Point pPtrRelPrnt, Point pCmpRelPrnt) {
		// TODO Auto-generated method stub
		return false;
	}
    


}
