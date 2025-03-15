package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class MapCommand extends Command {
    private GameWorld gw;

    public MapCommand(GameWorld gw) {
        super("Map");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        gw.printMap();  // Call the dedicated printMap() method
    }
}
