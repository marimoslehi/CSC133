package com.mycompany.a5;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point2D;
import com.codename1.charts.util.ColorUtil;
import java.util.ArrayList;

public abstract class GameObject implements ICollider {
    // Transform fields for local transformations
    private Transform myTranslate;
    private Transform myRotate;
    private Transform myScale;
    
    private int size;
    private int color;
    private ArrayList<IObserver> observers;
    private ArrayList<GameObject> collisionList;

    public GameObject(int size, int color) {
        this.size = size;
        this.color = color;
        observers = new ArrayList<>();
        collisionList = new ArrayList<>();
        
        // Initialize transforms
        myTranslate = Transform.makeIdentity();
        myRotate = Transform.makeIdentity();
        myScale = Transform.makeIdentity();
    }

    // Transform methods
    public void translate(float dx, float dy) {
        Transform tm = Transform.makeTranslation(dx, dy);
        myTranslate.concatenate(tm);
        notifyObservers();
    }
    
    public void rotate(float degrees) {
        Transform rm = Transform.makeRotation((float)Math.toRadians(degrees), 0, 0, 1); // Added required arguments
        myRotate.concatenate(rm);
        notifyObservers();
    }
    
    public void scale(float sx, float sy) {
        Transform sm = Transform.makeScale(sx, sy);
        myScale.concatenate(sm);
        notifyObservers();
    }

    // Get combined local transformation
    protected Transform getLocalTransform() {
        Transform combined = Transform.makeIdentity();
        combined.concatenate(myTranslate);
        combined.concatenate(myRotate);
        combined.concatenate(myScale);
        return combined;
    }

    // Updated to use transform
    public Point2D getLocation() {
        return new Point2D(myTranslate.getTranslateX(), myTranslate.getTranslateY());
    }

    public int getSize() {
        // Size should now consider scale transform
        float scaleX = myScale.getScaleX();
        return (int)(size * scaleX);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyObservers();
    }

    @Override
    public String toString() {
        Point2D loc = getLocation();
        return "GameObject: loc=(" + loc.getX() + ", " + loc.getY() + 
               "), size=" + getSize() + 
               ", color=[" + ColorUtil.red(color) + "," + 
               ColorUtil.green(color) + "," + ColorUtil.blue(color) + "]";
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
            observer.update(this);
        }
    }

    // Updated collision detection using transformed coordinates
    @Override
    public boolean collidesWith(GameObject otherObject) {
        Point2D myLoc = this.getLocation();
        Point2D otherLoc = otherObject.getLocation();
        double dx = myLoc.getX() - otherLoc.getX();
        double dy = myLoc.getY() - otherLoc.getY();
        double distanceSquared = dx * dx + dy * dy;
        double radiusSum = (this.getSize() / 2.0) + (otherObject.getSize() / 2.0);
        return distanceSquared <= radiusSum * radiusSum;
    }

    @Override
    public void handleCollision(GameObject otherObject) {
        if (!collisionList.contains(otherObject)) {
            collisionList.add(otherObject);
            if (otherObject instanceof Alien && this instanceof Alien) {
                System.out.println("Alien collided with Alien! Creating a new Alien.");
            } else if (otherObject instanceof Alien && this instanceof Astronaut) {
                System.out.println("Alien collided with Astronaut! Reducing health and speed.");
                Astronaut astronaut = (Astronaut) this;
                astronaut.decreaseHealth();
                astronaut.fadeColor();
                astronaut.reduceSpeed();
            }
        }
    }

    public void clearCollisions() {
        collisionList.clear();
    }

    // Abstract method for drawing
    public abstract void draw(Graphics g, Point pCmpRelPrnt);

    // Updated contains method for selection with transformations
    public boolean contains(Point pPtrRelPrnt, Point pCmpRelPrnt) {
        // Convert pointer coordinates to local space
        Transform worldToLocal = getLocalTransform().copy();
        try {
            worldToLocal.invert();
            
            // Get pointer position relative to component
            float px = pPtrRelPrnt.getX() - pCmpRelPrnt.getX();
            float py = pPtrRelPrnt.getY() - pCmpRelPrnt.getY();
            
            // Apply inverse transform by manually calculating the point transformation
            float x = worldToLocal.getTranslateX() + px * worldToLocal.getScaleX();
            float y = worldToLocal.getTranslateY() + py * worldToLocal.getScaleY();
            
            // Check if transformed point is within object's bounds in local space
            float halfSize = size / 2.0f;
            return Math.abs(x) <= halfSize && Math.abs(y) <= halfSize;
            
        } catch (Transform.NotInvertibleException e) {
            return false;
        }
    }

    public boolean isSelected() {
        return false;
    }

    public void setSelected(boolean selected) {
        // Implementation in subclasses if needed
    }

	public void move(int elapsedTimeInMillis) {
		// TODO Auto-generated method stub
		
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	
}