package com.mycompany.a5;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.ui.Transform;
import com.codename1.ui.plaf.Border;
import com.codename1.charts.util.ColorUtil;
import java.util.Observable;
import java.util.Observer;

public class MapView extends Container implements Observer {
    private Transform worldToND, ndToDisplay, theVTM;
    private float winLeft, winRight, winTop, winBottom;
    private float zoomFactor = 1.0f;
    private Point2D lastPointerLocation;
    private boolean isPanning = false;
    private boolean isZooming = false;
	private Object gameObjects;

    public MapView() {
        this.getAllStyles().setBorder(Border.createLineBorder(2, ColorUtil.rgb(255, 0, 0)));
        
        // Initialize transforms
        worldToND = Transform.makeIdentity();
        ndToDisplay = Transform.makeIdentity();
        theVTM = Transform.makeIdentity();
        
        // Initialize window boundaries
        // These will be properly set after show() is called
        winLeft = 0;
        winBottom = 0;
        winRight = 1000;  // temporary value
        winTop = 1000;    // temporary value
        
        // Set up input handling
        setupInputListeners();
    }
    
    private void setupInputListeners() {
        // Add pointer pressed listener
        addPointerPressedListener(evt -> {
            int x = evt.getX();
            int y = evt.getY();
            lastPointerLocation = new Point2D(x, y);
            
            if (evt.isLongEvent()) {  // Changed to use CN1's button check
                isZooming = true;
            } else {
                isPanning = true;
            }
        });
        
        // Add pointer dragged listener
        addPointerDraggedListener(evt -> {
            if (lastPointerLocation == null) return;
            
            int x = evt.getX();
            int y = evt.getY();
            float deltaX = x - (float)lastPointerLocation.getX();
            float deltaY = y - (float)lastPointerLocation.getY();
            
            if (isZooming) {
                handleZoom(deltaX, deltaY);
            } else if (isPanning) {
                handlePan(deltaX, deltaY);
            }
            
            lastPointerLocation = new Point2D(x, y);
            repaint();
        });
        
        // Add pointer released listener
        addPointerReleasedListener(evt -> {
            isPanning = false;
            isZooming = false;
            lastPointerLocation = null;
        });
    }
    
    private void handleZoom(float deltaX, float deltaY) {
        // Calculate zoom based on diagonal movement
        float zoomDelta = (deltaX + deltaY) / 200.0f; // Adjust sensitivity
        float newZoomFactor = zoomFactor * (1 + zoomDelta);
        
        // Limit zoom range
        if (newZoomFactor >= 0.25f && newZoomFactor <= 4.0f) {
            zoomFactor = newZoomFactor;
            
            // Update window boundaries for zoom
            float centerX = (winLeft + winRight) / 2;
            float centerY = (winTop + winBottom) / 2;
            float newWidth = (winRight - winLeft) / zoomFactor;
            float newHeight = (winTop - winBottom) / zoomFactor;
            
            winLeft = centerX - newWidth / 2;
            winRight = centerX + newWidth / 2;
            winBottom = centerY - newHeight / 2;
            winTop = centerY + newHeight / 2;
            
            // Check zoom out limit
            if (winRight - winLeft > 1000 || winTop - winBottom > 1000) {
                // Revert changes if exceeded limit
                zoomFactor = 1.0f;
                setupInitialWindow();
            }
            
            buildVTM();
        }
    }
    
    private void handlePan(float deltaX, float deltaY) {
        // Convert screen delta to world delta
        float worldDeltaX = deltaX * (winRight - winLeft) / getWidth();
        float worldDeltaY = deltaY * (winTop - winBottom) / getHeight();
        
        // Update window boundaries for pan
        winLeft -= worldDeltaX;
        winRight -= worldDeltaX;
        winBottom += worldDeltaY;
        winTop += worldDeltaY;
        
        buildVTM();
    }
    
    public void setupInitialWindow() {
        // This should be called after show()
        winLeft = 0;
        winBottom = 0;
        winRight = getWidth() / 2.0f;
        winTop = getHeight() / 2.0f;
        buildVTM();
    }
    
    private void buildVTM() {
        // Build world-to-ND transform
        float sx = 2.0f / (winRight - winLeft);
        float sy = 2.0f / (winTop - winBottom);
        worldToND = Transform.makeScale(sx, sy);
        worldToND.translate(-winLeft, -winBottom);
        
        // Build ND-to-display transform
        float px = getWidth() / 2.0f;
        float py = getHeight() / 2.0f;
        ndToDisplay = Transform.makeTranslation(px, py);
        ndToDisplay.scale(px, -py);
        
        // Build VTM
        theVTM = ndToDisplay.copy();
        theVTM.concatenate(worldToND);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
      

        // Reset graphics transform
        g.resetAffine();

        // Get relative position of the component
        Point pCmpRelPrnt = new Point(getX(), getY());

        // Apply the Viewing Transformation Matrix (VTM)
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(pCmpRelPrnt.getX(), pCmpRelPrnt.getY());
        gXform.concatenate(theVTM);
        g.setTransform(gXform);

        // Draw game objects
        GameWorld gw = GameWorld.getInstance();
        if (gw != null) {
            GameObjectCollection gameObjects = gw.getGameObjects();
            if (gameObjects != null) {
                IIterator it = gameObjects.getIterator();
                if (it != null) {
                    while (it.hasNext()) {
                        GameObject obj = it.getNext();
                        if (obj instanceof IDrawable) {
                            ((IDrawable) obj).draw(g, pCmpRelPrnt);
                        }
                    }
                }
            }
        }
        
      
        // Reset transform
        g.resetAffine();
    }
    
    @Override
    public void pointerPressed(int x, int y) {
        Point pPtrRelPrnt = new Point(x - getAbsoluteX(), y - getAbsoluteY());
        Point pCmpRelPrnt = new Point(getX(), getY());
        
        // Convert screen point to world coordinates
        Point2D screenPoint = new Point2D(pPtrRelPrnt.getX(), pPtrRelPrnt.getY());
        Transform inverseVTM = theVTM.copy();
        try {
            inverseVTM.invert();
            // Apply transform directly using x and y coordinates
            float worldX = (float)screenPoint.getX();
            float worldY = (float)screenPoint.getY();
            inverseVTM.setTranslation(worldX, worldY, 0);
            
            // Use the transformed coordinates
            GameWorld.getInstance().selectAstronautAt(new Point(
                (int)worldX, 
                (int)worldY
            ));
            repaint();
        } catch (Transform.NotInvertibleException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof GameWorld) {
            repaint();
        }
    }
}