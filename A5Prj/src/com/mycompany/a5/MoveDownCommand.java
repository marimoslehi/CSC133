package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class MoveDownCommand extends Command {
    private GameWorld gw;  // Reference to the game world

    // Constructor to initialize the command with a reference to GameWorld
    public MoveDownCommand(GameWorld gw) {
        super("Move Down");  // Set the name of the command
        this.gw = gw;
    }

    // Override the actionPerformed method to define the command's behavior
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Call the moveDown method on the spaceship through GameWorld
        gw.moveSpaceshipDown();
    }
}

