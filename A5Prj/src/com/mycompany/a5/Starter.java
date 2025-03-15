package com.mycompany.a5;

import com.codename1.ui.Display;
import com.codename1.ui.Form;

public class Starter {

    private Form current;

    // Initialization method (can include any additional logic if needed)
    public void init(Object context) {
        // Optional initialization logic (like theme loading, if required)
    }

    // Called when the application starts or resumes
    public void start() {
        // If the current form is not null (i.e., the app was minimized), show the current form
        if (current != null) {
            current.show();
            return;
        }

        // Start a new game if the app is starting for the first time
        new Game(); // Game class initializes the game world and starts the GUI
    }

    // Called when the application is minimized or paused
    public void stop() {
        // Store the current form so it can be restored when the app resumes
        current = Display.getInstance().getCurrent();
    }

    // Called when the application is destroyed
    public void destroy() {
        // Optional cleanup logic if needed
    }
}
