package game.entities.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import game.entities.Hitbox;
import game.manager.ResourceManager;

public class WoodProyectile extends AbstractObstacle {
    private Image image;
    private double speedMultiplier = 1.6; // ¡Viaja un 60% más rápido que la pantalla!

    public WoodProyectile(int x, int y) {
        // Hitbox chiquita para que sea justo saltarlo
        super(x, y, 24, 24, new Hitbox(4, 4, 16, 16));
        image = ResourceManager.getImage("/resources/sprites/Bullet.png");
    }

    @Override
    public void update(double deltaTime, int currentSpeed) {
        // Multiplicamos la velocidad base para que adelante al árbol
        x -= deltaTime * (currentSpeed * speedMultiplier);
        hitbox.update(x, y);
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillOval(x, y, width, height);
        }
    }
}
