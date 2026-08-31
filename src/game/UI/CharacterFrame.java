package game.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import game.manager.ResourceManager;

public class CharacterFrame {

    private int width;
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
    }
}