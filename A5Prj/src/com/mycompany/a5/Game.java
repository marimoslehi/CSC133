package com.mycompany.a5;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.util.UITimer;

public class Game extends Form implements Runnable {

    private GameWorld gw;
    private MapView mv;
    private ScoreView sv;
    private UITimer timer;

    private Button pauseButton;
    private Button healButton;
    private Button expandButton;
    private Button contractButton;
    private Button upButton;
    private Button downButton;
    private Button leftButton;
    private Button rightButton;
    private Button moveToAstronautButton;
    private Button moveToAlienButton;
    private Button scoreButton;
    private CheckBox soundCheckBox;

    private Label soundStatusLabel; // Toolbar sound status
    private boolean isPaused = false;

    public Game() {
        this.setLayout(new BorderLayout());
        gw = GameWorld.getInstance();

        mv = new MapView();
        sv = new ScoreView();

        gw.addObserver(mv);
        gw.addObserver(sv);

        this.add(BorderLayout.NORTH, sv);
        this.add(BorderLayout.CENTER, mv);

        mv.getAllStyles().setBorder(Border.createLineBorder(3, ColorUtil.BLUE));

        setupToolbar();
        setupControls();
        setupKeyBindings();

        gw.init();
        timer = new UITimer(this);
        timer.schedule(20, true, this);

        this.show();
    }

    @Override
    public void run() {
        if (!isPaused) {
            gw.tick(20);
            mv.repaint();
        }
    }

    private void setupToolbar() {
        Toolbar toolBar = new Toolbar();
        this.setToolbar(toolBar);
        

        // Add a sound status label
        soundStatusLabel = new Label("Sound: OFF");
        soundStatusLabel.getAllStyles().setFgColor(ColorUtil.WHITE);
        toolBar.add(BorderLayout.EAST, soundStatusLabel);

        healButton = createLargeStyledButton("Heal", evt -> {
            if (isPaused) {
                gw.healSelectedAstronaut(); // Attempt to heal the selected astronaut
            } else {
                System.out.println("Game must be paused to heal astronauts.");
            }
        });

        // Add Exit command
        toolBar.addCommandToSideMenu(new Command("Exit") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                if (Dialog.show("Exit", "Do you want to exit?", "Yes", "No")) {
                    com.codename1.ui.Display.getInstance().exitApplication();
                }
            }
        });

        // Add sound CheckBox
        soundCheckBox = new CheckBox("Sound");
        soundCheckBox.setSelected(false);

        // Synchronize sound state
        soundCheckBox.addActionListener(evt -> {
            boolean isSoundOn = soundCheckBox.isSelected();

            // Update GameWorld sound state
            gw.setSoundOn(isSoundOn);

            // Update ScoreView sound status
            sv.updateSoundStatus(isSoundOn);

            // Update toolbar sound status
            updateToolbarSoundStatus(isSoundOn);

            System.out.println("Sound state updated: " + (isSoundOn ? "ON" : "OFF"));
        });

        toolBar.addComponentToSideMenu(soundCheckBox);
    }

    private void updateToolbarSoundStatus(boolean isSoundOn) {
        soundStatusLabel.setText("Sound: " + (isSoundOn ? "ON" : "OFF"));
        soundStatusLabel.repaint(); // Refresh the label
    }

    private void setupControls() {
        Container leftContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container rightContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container bottomContainer = new Container(new FlowLayout(Component.CENTER));

        leftContainer.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.BLACK));
        rightContainer.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.BLACK));
        bottomContainer.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.BLACK));

        expandButton = createLargeStyledButton("Expand", evt -> gw.expandSpaceshipDoor());
        upButton = createLargeStyledButton("Up", evt -> gw.moveSpaceshipUp());
        leftButton = createLargeStyledButton("Left", evt -> gw.moveSpaceshipLeft());
        moveToAstronautButton = createLargeStyledButton("MoveToAstronaut", evt -> gw.moveSpaceshipToAstronaut());
        leftContainer.addAll(expandButton, upButton, leftButton, moveToAstronautButton);

        contractButton = createLargeStyledButton("Contract", evt -> gw.contractSpaceshipDoor());
        downButton = createLargeStyledButton("Down", evt -> gw.moveSpaceshipDown());
        rightButton = createLargeStyledButton("Right", evt -> gw.moveSpaceshipRight());
        moveToAlienButton = createLargeStyledButton("MoveToAlien", evt -> gw.moveSpaceshipToAlien());
        scoreButton = createLargeStyledButton("Score", evt -> gw.displayScore());
        rightContainer.addAll(contractButton, downButton, rightButton, moveToAlienButton, scoreButton);

        pauseButton = createLargeStyledButton("Pause", new com.codename1.ui.events.ActionListener() {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                togglePause();
            }
        });

        healButton = createLargeStyledButton("Heal", new com.codename1.ui.events.ActionListener() {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                if (isPaused) {
                    gw.healSelectedAstronaut();
                } else {
                    System.out.println("Game must be paused to heal astronauts.");
                }
            }
        });

        healButton.setEnabled(false); // Only enable when paused
        bottomContainer.addAll(healButton, pauseButton);

        this.add(BorderLayout.WEST, leftContainer);
        this.add(BorderLayout.EAST, rightContainer);
        this.add(BorderLayout.SOUTH, bottomContainer);
    }

    private void setupKeyBindings() {
        Command expandCommand = new Command("Expand") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.expandSpaceshipDoor();
            }
        };

        Command contractCommand = new Command("Contract") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.contractSpaceshipDoor();
            }
        };

        Command moveRightCommand = new Command("Move Right") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.moveSpaceshipRight();
            }
        };

        Command moveLeftCommand = new Command("Move Left") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.moveSpaceshipLeft();
            }
        };

        Command moveUpCommand = new Command("Move Up") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.moveSpaceshipUp();
            }
        };

        Command moveDownCommand = new Command("Move Down") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.moveSpaceshipDown();
            }
        };

        Command moveToAstronautCommand = new Command("Move to Astronaut") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.moveSpaceshipToAstronaut();
            }
        };

        Command moveToAlienCommand = new Command("Move to Alien") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.moveSpaceshipToAlien();
            }
        };

        Command scoreCommand = new Command("Score") {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent evt) {
                gw.displayScore();
            }
        };

        // Add key bindings using the commands
        this.addKeyListener('e', expandCommand);
        this.addKeyListener('c', contractCommand);
        this.addKeyListener('r', moveRightCommand);
        this.addKeyListener('l', moveLeftCommand);
        this.addKeyListener('u', moveUpCommand);
        this.addKeyListener('d', moveDownCommand);
        this.addKeyListener('o', moveToAstronautCommand);
        this.addKeyListener('a', moveToAlienCommand);
        this.addKeyListener('s', scoreCommand);
    }

    private Button createLargeStyledButton(String text, com.codename1.ui.events.ActionListener listener) {
        Button button = new Button(text);
        button.getAllStyles().setBgTransparency(255);
        button.getAllStyles().setBgColor(ColorUtil.BLUE);
        button.getAllStyles().setFgColor(ColorUtil.WHITE);
        button.getAllStyles().setPadding(Component.TOP, 20);
        button.getAllStyles().setPadding(Component.BOTTOM, 20);
        button.getAllStyles().setPadding(Component.LEFT, 10);
        button.getAllStyles().setPadding(Component.RIGHT, 10);
        button.getAllStyles().setMargin(10, 10, 10, 10);
        button.getAllStyles().setBorder(Border.createLineBorder(3, ColorUtil.WHITE));
        button.addActionListener(listener);
        return button;
    }

    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            pauseButton.setText("Play");
            timer.cancel();
            gw.togglePause();
            healButton.setEnabled(true);
            disableGameControls(true);
        } else {
            pauseButton.setText("Pause");
            timer.schedule(20, true, this);
            gw.togglePause();
            healButton.setEnabled(false);
            disableGameControls(false);
        }
    }

    private void disableGameControls(boolean disable) {
        expandButton.setEnabled(!disable);
        contractButton.setEnabled(!disable);
        upButton.setEnabled(!disable);
        downButton.setEnabled(!disable);
        leftButton.setEnabled(!disable);
        rightButton.setEnabled(!disable);
        moveToAstronautButton.setEnabled(!disable);
        moveToAlienButton.setEnabled(!disable);
        scoreButton.setEnabled(!disable);
    }
}
