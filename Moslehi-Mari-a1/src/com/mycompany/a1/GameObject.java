package com.mycompany.a1;

import com.codename1.charts.models.Point;


public abstract class GameObject {
    private int size;
    private Point location;
    private int color;

    // Constructor to initialize common attributes
    public GameObject(int size, Point location, int color) {
        this.size = size;
        this.location = location;
        this.color = color;
    }

    // Accessor methods for common attributes
    public int getSize() { return size; }
    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }
    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    // Abstract toString method to be implemented by subclasses
    public abstract String toString();
}

