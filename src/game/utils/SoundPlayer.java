package game.utils;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class SoundPlayer {

    public enum Sound { JUMP, COIN, GAME_OVER, MUSIC, HURT }

    private Clip clip;
    private final URL[] soundUrls = new URL[Sound.values().length];

    public SoundPlayer() {
        soundUrls[Sound.JUMP.ordinal()] = getClass().getResource("/resources/sfx/game-jump.wav");
        soundUrls[Sound.COIN.ordinal()] = getClass().getResource("/resources/sfx/coin.wav");
        soundUrls[Sound.GAME_OVER.ordinal()] = getClass().getResource("/resources/sfx/game-over.wav");
        soundUrls[Sound.MUSIC.ordinal()] = getClass().getResource("/resources/sfx/background-music.wav");
        soundUrls[Sound.HURT.ordinal()] = getClass().getResource("/resources/sfx/game-over.wav");
    }

    public void setFile(Sound sound) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrls[sound.ordinal()]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error: Could not upload the sound file " + e.getMessage());
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playAndWait() {
        if (clip != null) {
            clip.start();
            try {
                while (!clip.isRunning()) Thread.sleep(10);
                while (clip.isRunning()) Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
