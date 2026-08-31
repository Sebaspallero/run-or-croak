package game.entities.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.imageio.ImageIO;

import game.entities.Hitbox;
import game.entities.character.Character;
import game.manager.ResourceManager;
import game.utils.Animator;

public class Bee extends AbstractObstacle {

    private int minY, maxY; // Vertical movement limits
    private int directionY; // Direction of vertical movement (1 for down, -1 for up)
    private Animator animator;

    public Bee(int startX, int minY, int maxY) {
        super(startX, 0, 72, 68, new Hitbox(11, 16, 50, 35));

        Random random = new Random();

        // Set the initial Y position within the range [minY, maxY]
        this.minY = minY;
        this.maxY = maxY;
        this.y = random.nextInt(maxY - minY) + minY;

        // Initial random direction (1 for down, -1 for up)
        this.directionY = random.nextBoolean() ? 1 : -1;

        Image beeSpriteSheet = ResourceManager.getImage("/resources/sprites/bee-idle.png");
        if (beeSpriteSheet != null) {
            this.animator = new Animator(beeSpriteSheet,  36, 34, 6, 100, 0);
        }
    }

    public void update(double deltaTime, int currentSpeed, Character character) {
        x -= deltaTime * currentSpeed;

        if (character == null) {
            // Basic movement logic without reference to the character
            y += directionY * 2; // Simple vertical movement
            if (y < minY || y > maxY) {
                directionY *= -1; // Change direction if it reaches the limits
            }
        } else {
            // Movement logic towards the character
            Random random = new Random();
            int randomTargetRange = 50; // Deviation range in ±50 pixels
            int targetY = character.getCharacterY() + random.nextInt(randomTargetRange * 2) - randomTargetRange;

            // Smooth movement towards the target Y
            if (y < targetY) {
                y += 2;
            } else if (y > targetY) {
                y -= 2;
            }

            // Keep within the allowed limits
            if (y < minY) {
                y = minY;
            } else if (y > maxY) {
                y = maxY;
            }
        }

        // Update hitbox
        hitbox.update(x + 20, y + 10);

        // Update the animation
        if (animator != null) {
            animator.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (animator != null) {
            animator.draw(g, x, y, width, height);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height);
        }
    }

    @Override
    public void update(double deltaTime, int currentSpeed) {
        // Delegate to the version with character
        update(deltaTime, currentSpeed, null);
        hitbox.update(this.x, this.y);
    }

}