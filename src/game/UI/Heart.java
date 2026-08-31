package game.UI;

import game.manager.ResourceManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;

public class Heart {
    private int width;
    private int height;
    private int x;
    private int y;
    private boolean isFull;
    private Image heart;

    public Heart(int x, boolean isFull) {
        this.width = 12 * 2;
        this.height = 12 * 2;
        this.x = x;
        this.y = 25;
        this.isFull = isFull;

        initializeHeartImage();
    }

    public void initializeHeartImage() {
        try {
            if (isFull) {
                heart = ResourceManager.getImage("/resources/sprites/full-heart.png");;
            } else {
                heart = ResourceManager.getImage("/resources/sprites/empty-heart.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFull(boolean isFull) {
        this.isFull = isFull;
        initializeHeartImage();
    }

    public boolean isFull() {
        return this.isFull;
    }

    public void draw(Graphics g) {
        try {
            g.drawImage(heart, x, y, width, height, null);
        } catch (Exception e) {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
        }
    }

}
