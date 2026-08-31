package game.entities.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


import game.entities.Hitbox;
import game.manager.ResourceManager;

public class Spike extends AbstractObstacle {

    private Image image;

    public Spike(int x, int y) {
        super(x, y, 48, 48, new Hitbox(13, 8, 22, 40));
        image = ResourceManager.getImage("/resources/sprites/spikes-001.png");

    }

    @Override
    public void update(double deltaTime, int currentSpeed) {
        x -= deltaTime * currentSpeed;
        hitbox.update(this.x, this.y);
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }

}
