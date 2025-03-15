package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class TickCommand extends Command {
    private GameWorld gw;  // Reference to the game world

    // Constructor to initialize the command with a reference to GameWorld
    public TickCommand(GameWorld gw) {
        super("Tick");  // Set the name of the command
        this.gw = gw;
    }

    // Override the actionPerformed method to define the command's behavior
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Call the tick method on the game world
        gw.tick();
    }
}
