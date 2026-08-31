package game.entities.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import game.entities.Hitbox;
import game.manager.ResourceManager;

public class HeartItem extends AbstractItem {
    private Image image;

    public HeartItem(int x, int y) {
        // Un ítem pequeño y valioso en el aire
        super(x, y, 32, 32, new Hitbox(0, 0, 32, 32));
        image = ResourceManager.getImage("/resources/sprites/full-heart.png");
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
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }
    }
}