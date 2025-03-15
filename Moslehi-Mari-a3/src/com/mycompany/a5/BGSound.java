package com.mycompany.a5;

import java.io.IOException;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;

public class BGSound {
    private Media media;

    public BGSound(String fileName) {
        try {
            // MediaManager.createMedia expects a path to the resource
            media = MediaManager.createMedia(fileName, true); // true for looping
        } catch (IOException e) {
            System.out.println("Error loading background music file: " + fileName);
            e.printStackTrace();
        }
    }

    public void play() {
        if (media != null) {
            media.play();
        } else {
            System.out.println("Background music is null, unable to play.");
        }
    }

    public void pause() {
        if (media != null) {
            media.pause();
        }
    }
}
