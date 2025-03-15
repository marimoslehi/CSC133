package com.mycompany.a5;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.util.UITimer;

public class PauseCommand extends Command {
    private Game game;
    private UITimer timer;
    private boolean isPaused;

    public PauseCommand(Game game, UITimer timer) {
        super("Pause");
        this.game = game;
        this.timer = timer;
        this.isPaused = false;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        isPaused = !isPaused;
        if (isPaused) {
            timer.cancel(); // Stop animation
            System.out.println("Game paused.");
        } else {
            timer.schedule(20, true, game); // Resume animation
            System.out.println("Game resumed.");
        }
    }
}

