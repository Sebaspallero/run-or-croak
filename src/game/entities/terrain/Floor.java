package game.entities.terrain;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.main.Game;
import game.manager.ResourceManager;

public class Floor {

    private int x, y, width, height;
    private int screen_width;
    private Image image;

    public Floor() {
        this.screen_width = Game.SCREEN_WIDTH;
        this.width = 750 * 2;
        this.height = 60 * 2;
        this.x = 0;
        this.y = Game.SCREEN_HEIGHT - 120;
        image = ResourceManager.getImage("/resources/sprites/floor-001.png");
    }

    public void update(double deltaTime, int currentSpeed) {
        x -= deltaTime * currentSpeed;
        if (x + width <= 0) {
            x = 0;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);

        if (x + width < screen_width) {
            g.drawImage(image, x + width, y, width, height, null);
        }
    }
}
