package com.mycompany.a5;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;

public class Sound {
    private Media sound;

    public Sound(String filePath) {
        try {
            // MediaManager.createMedia expects a path to the resource
            sound = MediaManager.createMedia(filePath, false);
        } catch (Exception e) {
            System.out.println("Error loading sound file: " + filePath);
            e.printStackTrace();
        }
    }

    public void play() {
        if (sound != null) {
            sound.setTime(0); // Reset to start
            sound.play();
        } else {
            System.out.println("Sound is null, unable to play.");
        }
    }
}
