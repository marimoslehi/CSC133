package com.mycompany.a1;

import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

public class Game extends Form {
    private GameWorld gw;
    private boolean exitRequested = false;

    public Game() {
        gw = new GameWorld();
        gw.init();
        play();
    }

    private void play() {
        Label myLabel = new Label("Enter a Command:");
        this.addComponent(myLabel);

        final TextField myTextField = new TextField();
        this.addComponent(myTextField);
        this.show();

        myTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String sCommand = myTextField.getText().toString().trim();
                myTextField.clear();

                if (sCommand.isEmpty()) return;

                if (exitRequested) {
                    if (sCommand.equalsIgnoreCase("y")) {
                        System.out.println("Exiting game...");
                        System.exit(0);
                    } else if (sCommand.equalsIgnoreCase("n")) {
                        System.out.println("Exit canceled.");
                        exitRequested = false;
                    } else {
                        System.out.println("Invalid command. Enter 'y' to confirm exit or 'n' to cancel.");
                    }
                } else {
                    switch (sCommand.charAt(0)) {
                        case 'e': gw.expandSpaceshipDoor(); System.out.println("Spaceship door expanded."); break;
                        case 'c': gw.contractSpaceshipDoor(); System.out.println("Spaceship door contracted."); break;
                        case 'r': gw.moveSpaceshipRight(); System.out.println("Spaceship moved right."); break;
                        case 'l': gw.moveSpaceshipLeft(); System.out.println("Spaceship moved left."); break;
                        case 'u': gw.moveSpaceshipUp(); System.out.println("Spaceship moved up."); break;
                        case 'd': gw.moveSpaceshipDown(); System.out.println("Spaceship moved down."); break;
                        case 't': gw.tick(); System.out.println("The game clock has ticked."); break;
                        case 's': gw.openSpaceshipDoor(); break;
                        case 'x': System.out.println("Exit game? (y/n)"); exitRequested = true; break;
                        case 'o': gw.jumpToAstronaut(); break;
                        case 'a': gw.jumpToAlien(); break;
                        case 'w': gw.pretendAlienCollision(); break;
                        case 'f': gw.pretendFight(); break;
                        case 'p': gw.printGameState(); break;
                        case 'm': gw.printMap(); break;
                        default: System.out.println("Invalid command."); break;
                    }
                }
            }
        });
    }
}
