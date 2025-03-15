package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class ScoreCommand extends Command {
    private GameWorld gw;

    public ScoreCommand(GameWorld gw) {
        super("Score");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // Display the current score, or handle score display logic here
    	System.out.println("Score button clicked");
        gw.displayScore();
    }
}
