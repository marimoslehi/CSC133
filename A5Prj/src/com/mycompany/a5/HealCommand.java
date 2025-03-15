package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class HealCommand extends Command {
    private GameWorld gw;

    public HealCommand(GameWorld gw) {
        super("Heal");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        gw.healSelectedAstronaut(); // Implement healing logic in GameWorld
    }
}
