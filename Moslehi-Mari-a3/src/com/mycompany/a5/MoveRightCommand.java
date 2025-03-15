package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class MoveRightCommand extends Command {
    private GameWorld gw;  // Reference to the game world

    // Constructor to initialize the command with a reference to GameWorld
    public MoveRightCommand(GameWorld gw) {
        super("Move Right");  // Set the name of the command
        this.gw = gw;
    }

    // Override the actionPerformed method to define the command's behavior
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Call the moveRight method on the spaceship through GameWorld
        gw.moveSpaceshipRight();
    }
}
