package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class MoveUpCommand extends Command {
    private GameWorld gw;  // Reference to the game world

    // Constructor to initialize the command with a reference to GameWorld
    public MoveUpCommand(GameWorld gw) {
        super("Move Up");  // Set the name of the command
        this.gw = gw;
    }

    // Override the actionPerformed method to define the command's behavior
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Call the moveUp method on the spaceship through GameWorld
        gw.moveSpaceshipUp();
    }
}
