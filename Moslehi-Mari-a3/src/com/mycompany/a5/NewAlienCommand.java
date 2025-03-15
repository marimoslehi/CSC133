package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class NewAlienCommand extends Command {
    private GameWorld gw;

    public NewAlienCommand(GameWorld gw) {
        super("New Alien");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // Create a new alien in the game world
        gw.createNewAlien();

        // Print confirmation to the console
        System.out.println("New Alien created in the game.");
    }
}
