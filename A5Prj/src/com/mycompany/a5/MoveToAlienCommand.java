package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class MoveToAlienCommand extends Command {
    private GameWorld gw;

    public MoveToAlienCommand(GameWorld gw) {
        super("MoveToAlien");  // Set the name of the command
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // Call a method in GameWorld to move the spaceship to an alien
        gw.moveSpaceshipToAlien();
    }
}
