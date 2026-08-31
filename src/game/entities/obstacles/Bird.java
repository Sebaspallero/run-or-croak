package game.entities.obstacles;

import java.awt.Graphics;
import java.awt.Image;


import game.entities.Hitbox;
import game.manager.ResourceManager;
import game.utils.Animator;

public class Bird extends AbstractObstacle {
    private Animator animator;

    public Bird(int x, int y) {
        super(x, y, 64, 64, new Hitbox(16, 16, 32, 32));

        Image birdSpriteSheet = ResourceManager.getImage("/resources/sprites/blue-bird.png");
        if (birdSpriteSheet != null) {
            this.animator = new Animator(birdSpriteSheet, 32, 32, 9, 50, 0);
        }
    }

    @Override
    public void update(double deltaTime, int currentSpeed) {
        x -= deltaTime * currentSpeed;
        if (animator != null) animator.update();

        hitbox.update(this.x, this.y);
    }

    @Override
    public void draw(Graphics g) {
        if (animator != null) animator.draw(g, x, y, width, height);
    }

}
