package com.mycompany.a5;

import java.util.Random;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Alien extends GameObject implements IDrawable {
    private List<AlienPart> parts;
    private float speed;
    private float direction;
    private float legAngle;
    private boolean legSwingForward;
    private int health;
    private List<AlienPart> staticParts;
    private List<AlienPart> dynamicParts;

    private AlienPart body;
    private AlienPart head;
    private AlienPart leftArm;
    private AlienPart rightArm;
    private AlienPart leftLeg;
    private AlienPart rightLeg;
    private final Random random = new Random(); // You already have this defined

    public Alien(int size, int color) {
        super(size, color);
        this.health = 100;
        this.speed = this.random.nextFloat() * 2 + 1; // Use this.random
        this.direction = this.random.nextFloat() * 360; // Use this.random
        this.legAngle = 0;
        this.legSwingForward = true;
       
        staticParts = new ArrayList<>();
        dynamicParts = new ArrayList<>();
        
        parts = new ArrayList<>();
        initializeParts();
    }

    private void initializeParts() {
    	// Create larger parts with empty fill (just outlines)
        int baseSize = getSize();
        
        // Body should be oval shaped
        body = new AlienPart("Body", baseSize, baseSize * 3/2, ColorUtil.BLACK);
        
        // Head should be a triangle, slightly smaller than body width
        head = new AlienPart("Head", baseSize * 2/3, baseSize/2, ColorUtil.BLACK);
        head.translate(0, -baseSize); // Place above body
        
        // Arms should be rectangular, thinner than body
        leftArm = new AlienPart("Arm", baseSize/4, baseSize * 2/3, ColorUtil.BLACK);
        leftArm.translate(-baseSize * 2/3, -baseSize/3);
        
        rightArm = new AlienPart("Arm", baseSize/4, baseSize * 2/3, ColorUtil.BLACK);
        rightArm.translate(baseSize * 2/3, -baseSize/3);
        
        // Legs should be rectangular, similar to arms but maybe slightly thicker
        leftLeg = new AlienPart("Leg", baseSize/3, baseSize * 2/3, ColorUtil.BLACK);
        leftLeg.translate(-baseSize/3, baseSize/2);
        
        rightLeg = new AlienPart("Leg", baseSize/3, baseSize * 2/3, ColorUtil.BLACK);
        rightLeg.translate(baseSize/3, baseSize/2);
        
        // Add all parts
        parts.clear();
        parts.add(body);
        parts.add(head);
        parts.add(leftArm);
        parts.add(rightArm);
        parts.add(leftLeg);
        parts.add(rightLeg);
    }

    // Add these methods to fix the compilation errors
    public void addSubObject(AlienPart part) {
        if (part != null) {
            staticParts.add(part);
        }
    }
    
    public void addDynamicSubObject(AlienPart part) {
        if (part != null) {
            dynamicParts.add(part);
        }
    }
    
    @Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        
        // Apply alien's world transform
        g.transform(getLocalTransform());
        
        // Draw static parts
        for (AlienPart part : staticParts) {
            part.draw(g, pCmpRelPrnt);
        }
        
        // Draw dynamic parts
        for (AlienPart part : dynamicParts) {
            part.draw(g, pCmpRelPrnt);
        }
        
        g.setTransform(gXform);
        
        System.out.println("Drawing alien at: " + getLocation());
    }
    
    // Update move method to handle dynamic parts
    @Override
    public void move(int elapsedTimeInMillis) {
        // First update position
        float deltaX = (float)(Math.cos(Math.toRadians(direction)) * speed * (elapsedTimeInMillis/20.0));
        float deltaY = (float)(Math.sin(Math.toRadians(direction)) * speed * (elapsedTimeInMillis/20.0));
        
        // Debug print before translation
        System.out.println("Moving alien by: " + deltaX + ", " + deltaY);
        
        // Apply translation to move the alien
        translate(deltaX, deltaY);
        
        // Animate legs
        float legDelta = elapsedTimeInMillis / 100.0f;
        if (legSwingForward) {
            legAngle += legDelta;
            if (legAngle > 30) legSwingForward = false;
        } else {
            legAngle -= legDelta;
            if (legAngle < -30) legSwingForward = true;
        }
        
        // Update leg rotations
        if (leftLeg != null) leftLeg.rotate(legAngle);
        if (rightLeg != null) rightLeg.rotate(-legAngle);
        
        // Face direction of movement
        rotate(direction);
        
        // Debug print after movement
        Point2D newLoc = getLocation();
        System.out.println("New alien position: " + newLoc.getX() + ", " + newLoc.getY());
    }

// In GameWorld.java, here's how createHierarchicalAlien should look:
private void createHierarchicalAlien() {
    Alien alien = new Alien(40, ColorUtil.BLACK); // Adjust size and color as needed
    
    // Create body (static part)
    AlienPart body = new AlienPart("Body", 40, 60, ColorUtil.BLACK);
    alien.addSubObject(body);
    
    // Create head (static part)
    AlienPart head = new AlienPart("Head", 30, 30, ColorUtil.BLACK);
    head.translate(0, 40); // Position above body
    alien.addSubObject(head);
    
    // Create arms (static parts)
    AlienPart leftArm = new AlienPart("Arm", 15, 40, ColorUtil.BLACK);
    leftArm.translate(-30, 0); // Position on left side
    alien.addSubObject(leftArm);
    
    AlienPart rightArm = new AlienPart("Arm", 15, 40, ColorUtil.BLACK);
    rightArm.translate(30, 0); // Position on right side
    alien.addSubObject(rightArm);
    
    // Create legs (dynamic parts for animation)
    AlienPart leftLeg = new AlienPart("Leg", 15, 50, ColorUtil.BLACK);
    leftLeg.translate(-20, -40); // Position on bottom left
    alien.addDynamicSubObject(leftLeg);
    
    AlienPart rightLeg = new AlienPart("Leg", 15, 50, ColorUtil.BLACK);
    rightLeg.translate(20, -40); // Position on bottom right
    alien.addDynamicSubObject(rightLeg);
    
    // Add the alien to the game world
    addGameObject(alien);
}

private void addGameObject(Alien alien) {
	// TODO Auto-generated method stub
	
}

public void takeDamage(int i) {
	// TODO Auto-generated method stub
	
}
}