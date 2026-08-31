package game.entities.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;

import game.entities.Hitbox;
import game.utils.Animator;

public class Cherry extends AbstractItem {

    private Animator animator;

    private static Image idleSprite;
    private static Image collectedSprite;
    static {
        try {
            idleSprite = ImageIO.read(Cherry.class.getResource("/resources/sprites/cherries.png"));
            collectedSprite = ImageIO.read(Cherry.class.getResource("/resources/sprites/collected.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading Cherry sprites");
        }
    }

    public Cherry(int x, int y) {
        super(x, y, 64, 64, new Hitbox(18, 18, 28, 28));
        this.animator = new Animator(idleSprite, 64, 64, 17, 80, 0); // Configura el Animator

    }

    @Override
    public void update(double deltaTime, int currentSpeed) {
        x -= deltaTime * currentSpeed;
        if (animator != null) {
            animator.update();
        }
        hitbox.update(x + 20, y + 20);
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

    @Override
    public void setCurrentState(State newState) {
        if (newState != currentState) {
            this.currentState = newState;
            switch (newState) {
                case IDLE:
                    animator = new Animator(idleSprite, width, height, 17, 50, 0);
                    break;
                case COLLECTED:
                    animator = new Animator(collectedSprite, width, height, 17, 50, 0);
                    break;
            }
        }
    }

}