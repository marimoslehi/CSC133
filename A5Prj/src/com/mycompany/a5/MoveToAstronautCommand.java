package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class MoveToAstronautCommand extends Command {
    private GameWorld gw;

    public MoveToAstronautCommand(GameWorld gw) {
        super("MoveToAstronaut");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // Call a method in GameWorld to move the spaceship to an astronaut
        gw.moveSpaceshipToAstronaut();
    }
}
