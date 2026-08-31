package game.UI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import game.manager.ResourceManager;

public class SoundToggle {

    /* private int width;
    private int height;
    private int x;
    private int y;
    private Image image;

    public CharacterFrame() {
        this.width = 48;
        this.height = 48;
        this.x = 10;
        this.y = 10;

        this.image = ResourceManager.getImage("/resources/sprites/character-frame.png");
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
        }
    } */

    private int width;
    private int height;
    private int x;
    private int y;
    private Image iconOn;
    private Image iconOff;
    private boolean isVolumeOn;

    public SoundToggle() {
        this.width = 30;
        this.height = 30;
        this.x = 700;
        this.y = 10;
        this.iconOn = ResourceManager.getImage("/resources/sprites/soundOnWhite.png");
        this.iconOff = ResourceManager.getImage("/resources/sprites/soundOffWhite.png");
        this.isVolumeOn = true;
    }


    public void draw(Graphics g) {
        if (iconOn != null && iconOff != null) {
            Image iconToDraw = this.isVolumeOn ? this.iconOn : this.iconOff;
            g.drawImage(iconToDraw, x, y, width, height, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
        }
    }

    public void toggleSound() {
        this.isVolumeOn = !this.isVolumeOn;
    }

    public boolean getIsVolumeOn() {
        return this.isVolumeOn;
    }


}
