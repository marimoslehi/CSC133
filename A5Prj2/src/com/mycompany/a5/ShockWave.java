package com.mycompany.a5;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.charts.util.ColorUtil;
import java.util.Random;

public class ShockWave extends GameObject implements IMove {
    private float speed;
    private float direction; // in degrees
    private int maxLifetime;
    private int currentLifetime;
    private Point2D[] controlPoints; // Bezier curve control points
    
    // Remove the unused constructor
    public ShockWave(Point2D location) {
        // Call GameObject constructor with size and color
        super(100, ColorUtil.rgb(255, 255, 0)); // Yellow color, size 100
        
        // Initialize random movement
        Random rand = new Random();
        this.speed = rand.nextFloat() * 2 + 1; // Speed between 1-3
        this.direction = rand.nextFloat() * 360; // Random direction 0-360
        
        // Set initial position
        translate((float)location.getX(), (float)location.getY());
        
        // Generate random control points for Bezier curve
        generateControlPoints();
        
        // Set lifetime (adjust these values based on your needs)
        this.maxLifetime = 200; // Approximately time to cross screen
        this.currentLifetime = 0;
    }
    
    private void generateControlPoints() {
        Random rand = new Random();
        controlPoints = new Point2D[4];
        
        // Generate random control points in local space
        // Point 0 is origin (center of shockwave)
        controlPoints[0] = new Point2D(0, 0);
        
        // Random points within reasonable bounds
        float bound = 50; // Adjust this value to change curve size
        for (int i = 1; i < 4; i++) {
            float x = (rand.nextFloat() * 2 - 1) * bound;
            float y = (rand.nextFloat() * 2 - 1) * bound;
            controlPoints[i] = new Point2D(x, y);
        }
    }
    
    @Override
    public void move() {  // Changed to match IMove interface
        // Update lifetime
        currentLifetime += 20;  // Default time step
        
        if (!isExpired()) {
            // Calculate movement based on speed and direction
            float deltaX = (float)(Math.cos(Math.toRadians(direction)) * speed);
            float deltaY = (float)(Math.sin(Math.toRadians(direction)) * speed);
            
            // Apply translation
            translate(deltaX, deltaY);
        }
    }
    
    public boolean isExpired() {
        return currentLifetime >= maxLifetime;
    }
    
    @Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        // Save current graphics state
        Transform oldTransform = Transform.makeIdentity();
        g.getTransform(oldTransform);
        
        // Perform "local origin" transform - part two
        g.translate(pCmpRelPrnt.getX(), pCmpRelPrnt.getY());
        
        // Append local transform
        Transform localTransform = Transform.makeIdentity();
        localTransform.setTranslation((float)getLocation().getX(), 
                                   (float)getLocation().getY(), 
                                   0);  // Changed from translate to setTransform
        g.transform(localTransform);
        
        // Set drawing color with fade based on lifetime
        float fadeRatio = 1 - ((float)currentLifetime / maxLifetime);
        int alpha = (int)(255 * fadeRatio);
        g.setColor(ColorUtil.argb(alpha, 255, 255, 0));
        
        // Draw Bezier curve using recursive subdivision
        drawBezierCurve(g, controlPoints, 0);
        
        // Restore graphics state
        g.setTransform(oldTransform);
    }
    
    private void drawBezierCurve(Graphics g, Point2D[] points, int level) {
        if (level > 5) { // Adjust recursion level for smoothness
            // Draw line segment
            g.drawLine(
                (int)points[0].getX(), (int)points[0].getY(),
                (int)points[3].getX(), (int)points[3].getY()
            );
        } else {
            // Calculate midpoints
            Point2D[] leftPoints = new Point2D[4];
            Point2D[] rightPoints = new Point2D[4];
            subdivideBezier(points, leftPoints, rightPoints);
            
            // Recursively draw both halves
            drawBezierCurve(g, leftPoints, level + 1);
            drawBezierCurve(g, rightPoints, level + 1);
        }
    }
    
    private void subdivideBezier(Point2D[] points, Point2D[] left, Point2D[] right) {
        // De Casteljau's algorithm
        Point2D[] temp = new Point2D[3];
        for (int i = 0; i < 3; i++) {
            temp[i] = new Point2D(
                (points[i].getX() + points[i + 1].getX()) / 2,
                (points[i].getY() + points[i + 1].getY()) / 2
            );
        }
        
        Point2D[] temp2 = new Point2D[2];
        for (int i = 0; i < 2; i++) {
            temp2[i] = new Point2D(
                (temp[i].getX() + temp[i + 1].getX()) / 2,
                (temp[i].getY() + temp[i + 1].getY()) / 2
            );
        }
        
        Point2D center = new Point2D(
            (temp2[0].getX() + temp2[1].getX()) / 2,
            (temp2[0].getY() + temp2[1].getY()) / 2
        );
        
        // Fill left and right curve control points
        left[0] = points[0];
        left[1] = temp[0];
        left[2] = temp2[0];
        left[3] = center;
        
        right[0] = center;
        right[1] = temp2[1];
        right[2] = temp[2];
        right[3] = points[3];
    }
    
    @Override
    public void handleCollision(GameObject otherObject) {
        // ShockWaves don't react to collisions as per requirements
    }
}