package game.entities.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;

import game.entities.Hitbox;
import game.manager.ResourceManager;
import game.utils.Animator;

public class Pig extends AbstractObstacle {
    private Animator animator;

    public Pig(int x, int y) {
        super(x, y, 72, 60, new Hitbox(18, 20, 36, 35));
        Image pigSpriteSheet = ResourceManager.getImage("/resources/sprites/pig-run.png");
        if (pigSpriteSheet != null) {
            this.animator = new Animator(pigSpriteSheet, 36, 30, 12, 50, 0);
        }
    }

    @Override
    public void update(double deltaTime, int currentSpeed) {
        x -= deltaTime * currentSpeed;
        if (animator != null) {
            animator.update();
        }
        hitbox.update(this.x, this.y);
    }

    @Override
    public void draw(Graphics g) {
        if (animator != null) {
            animator.draw(g, x, y, width, height);
            /* hitbox.draw(g); */
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }

}