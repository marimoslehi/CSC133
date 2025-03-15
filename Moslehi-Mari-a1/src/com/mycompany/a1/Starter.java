package com.mycompany.a1;




import com.codename1.ui.Display;
import com.codename1.ui.Form;

public class Starter {

    private Form current;

    // This method is required for initialization (even if you don't use it)
    public void init(Object context) {
        // Optional initialization logic, if any
    }

    // Called when the application starts
    public void start() {
        if (current != null) {
            current.show();
            return;
        }
        new Game(); // Start the game
    }

    // Called when the application is minimized or paused
    public void stop() {
        current = Display.getInstance().getCurrent();
    }

    // Called when the application is destroyed
    public void destroy() {
    }
}

