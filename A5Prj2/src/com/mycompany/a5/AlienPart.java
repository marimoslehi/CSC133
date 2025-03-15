// First, modify your AlienPart.java to include drawable interface:
package com.mycompany.a5;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;

public class AlienPart implements IDrawable {
    private String type;
    private int width, height;
    private int color;
    private Transform myTranslate;
    private Transform myRotate;
    private Transform myScale;

    public AlienPart(String type, int width, int height, int color) {
        this.type = type;
        this.width = width;
        this.height = height;
        this.color = color;
        this.myTranslate = Transform.makeIdentity();
        this.myRotate = Transform.makeIdentity();
        this.myScale = Transform.makeIdentity();
    }

    public void translate(float x, float y) {
        myTranslate.translate(x, y);
    }

    public void rotate(float degrees) {
        myRotate.rotate((float) Math.toRadians(degrees), 0, 0);
    }

    public void scale(float sx, float sy) {
        myScale.scale(sx, sy);
    }

    public Transform getLocalTransform() {
        Transform combined = Transform.makeIdentity();
        combined.concatenate(myTranslate);
        combined.concatenate(myRotate);
        combined.concatenate(myScale);
        return combined;
    }

    @Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        
        g.transform(getLocalTransform());
        
        g.setColor(color);
        switch(type) {
            case "Body":
                g.fillArc(-width/2, -height/2, width, height, 0, 360);
                break;
            case "Head":
                int[] xPoints = {0, -width/2, width/2};
                int[] yPoints = {-height/2, height/2, height/2};
                g.fillTriangle(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
                break;
            case "Arm":
            case "Leg":
                g.fillRect(-width/2, -height/2, width, height);
                break;
        }
        
        g.setTransform(gXform);
    }
}