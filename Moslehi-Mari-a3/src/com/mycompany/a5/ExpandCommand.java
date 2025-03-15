package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class ExpandCommand extends Command {
    private GameWorld gw;  // Reference to the game world, to access the spaceship

    // Constructor to initialize the command with a reference to GameWorld
    public ExpandCommand(GameWorld gw) {
        super("Expand");  // Set the name of the command
        this.gw = gw;
    }

    // Override the actionPerformed method to define the command's behavior
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Call the expand method on the spaceship through GameWorld
    	System.out.println("Expand command triggered");
        gw.expandSpaceshipDoor();
    }
}

