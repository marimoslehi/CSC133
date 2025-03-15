package com.mycompany.a5;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.plaf.Border;
import com.codename1.charts.util.ColorUtil;

import java.util.Observable;
import java.util.Observer;

public class MapView extends Container implements Observer {

    public MapView() {
        this.getAllStyles().setBorder(Border.createLineBorder(2, ColorUtil.rgb(255, 0, 0))); // 2px red border
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Point pCmpRelPrnt = new Point(this.getX(), this.getY());

        GameWorld gw = GameWorld.getInstance(); // Assuming GameWorld is a singleton
        if (gw != null) {
            GameObjectCollection gameObjects = gw.getGameObjects(); // Get game objects
            if (gameObjects != null) {
                IIterator it = gameObjects.getIterator();
                while (it.hasNext()) {
                    GameObject obj = it.getNext();
                    if (obj instanceof IDrawable) {
                        ((IDrawable) obj).draw(g, pCmpRelPrnt);
                    }
                }
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof GameWorld) {
            repaint();
        }
    }
    
    @Override
    public void pointerPressed(int x, int y) {
        // Convert screen coordinates to component-relative coordinates
        int adjustedX = x - getAbsoluteX();
        int adjustedY = y - getAbsoluteY();

        // Notify GameWorld to select an astronaut
        GameWorld.getInstance().selectAstronautAt(new Point(adjustedX, adjustedY));
        repaint();
    }
}
