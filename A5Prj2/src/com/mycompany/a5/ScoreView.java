package com.mycompany.a5;

import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.Font;
import com.codename1.charts.util.ColorUtil;

public class ScoreView extends Container {

    private Label timeLabel;
    private Label scoreLabel;
    private Label astronautsRescuedLabel;
    private Label aliensSneakedLabel;
    private Label astronautsRemainingLabel;
    private Label aliensRemainingLabel;
    private Label soundLabel;

    public ScoreView() {
        // Set the overall layout for the container
        this.setLayout(new BorderLayout());

        // Create and style the title label
        Label titleLabel = new Label("The Rescue Game");
        titleLabel.getAllStyles().setFgColor(ColorUtil.BLACK); // Black color
        titleLabel.getAllStyles().setAlignment(CENTER);
        titleLabel.getAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE)); // Large bold font
        titleLabel.getAllStyles().setPadding(10, 10, 10, 10); // Add spacing

        // Create the container for the score info and set layout
        Container infoBar = new Container(new FlowLayout(CENTER)); // Horizontal layout for the top bar

        // Initialize and style all labels
        timeLabel = createStyledLabel("Time: 0");
        scoreLabel = createStyledLabel("Score: 0");
        astronautsRescuedLabel = createStyledLabel("Astronauts Rescued: 0");
        aliensSneakedLabel = createStyledLabel("Aliens Sneaked In: 0");
        astronautsRemainingLabel = createStyledLabel("Astronauts Remaining: 4");
        aliensRemainingLabel = createStyledLabel("Aliens Remaining: 3");
        soundLabel = createStyledLabel("Sound: OFF");

        // Add all labels to the info bar
        infoBar.add(timeLabel);
        infoBar.add(scoreLabel);
        infoBar.add(astronautsRescuedLabel);
        infoBar.add(aliensSneakedLabel);
        infoBar.add(astronautsRemainingLabel);
        infoBar.add(aliensRemainingLabel);
        infoBar.add(soundLabel);

        // Add title and info bar to the container
        this.add(BorderLayout.NORTH, titleLabel);
        this.add(BorderLayout.CENTER, infoBar);

        // Add a border for the entire ScoreView (optional)
        this.getAllStyles().setBorder(Border.createLineBorder(2, ColorUtil.BLACK));
    }

    // Helper method to create and style labels with larger font
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.getAllStyles().setFgColor(ColorUtil.rgb(0, 0, 255)); // Blue text color
        label.getAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE)); // Larger font
        label.getAllStyles().setPadding(10, 10, 10, 10); // Add padding
        return label;
    }

    // Method to dynamically update label values
    public void updateScoreView(int time, int score, int astronautsRescued, int aliensSneaked,
                                int astronautsRemaining, int aliensRemaining, boolean isSoundOn) {
        timeLabel.setText("Time: " + time);
        scoreLabel.setText("Score: " + score);
        astronautsRescuedLabel.setText("Astronauts Rescued: " + astronautsRescued);
        aliensSneakedLabel.setText("Aliens Sneaked In: " + aliensSneaked);
        astronautsRemainingLabel.setText("Astronauts Remaining: " + astronautsRemaining);
        aliensRemainingLabel.setText("Aliens Remaining: " + aliensRemaining);
        soundLabel.setText("Sound: " + (isSoundOn ? "ON" : "OFF"));

        this.revalidate(); // Refresh the UI after updating
    }

    // Overloaded method to update sound only
    public void updateSoundStatus(boolean isSoundOn) {
        soundLabel.setText("Sound: " + (isSoundOn ? "ON" : "OFF"));
        this.revalidate(); // Refresh the UI after updating
    }
}
