package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class SoundCommand extends Command {
    private GameWorld gw;  // Reference to the game world

    // Constructor to initialize the command with a reference to GameWorld
    public SoundCommand(GameWorld gw) {
        super("Sound");  // Set the name of the command
        this.gw = gw;
    }

    // Override the actionPerformed method to define the command's behavior
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Toggle the sound on or off in the GameWorld
        boolean currentSoundStatus = gw.isSoundOn();
        gw.setSoundOn(!currentSoundStatus);  // Toggle sound state

        // Print out the new sound status for debugging purposes
        if (gw.isSoundOn()) {
            System.out.println("Sound is now ON");
        } else {
            System.out.println("Sound is now OFF");
        }

        // Notify the observers to update the game state view
        gw.notifyObservers();
    }
}

