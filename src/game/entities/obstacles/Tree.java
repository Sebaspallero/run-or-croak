package game.entities.obstacles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import game.entities.AbstractEntity;
import game.entities.Hitbox;
import game.manager.ResourceManager;
import game.utils.Animator;

public class Tree extends AbstractObstacle {
    private boolean hasAttacked;
    private AbstractEntity pendingProjectile; // El proyectil que está a punto de escupir
    private Animator currentAnimator;
    private Animator attackAnimator;
    private Animator idleAnimator;
    private static final int PROJECTILE_LAUNCH_FRAME = 6; // Lanza en el frame 6

    public enum State { IDLE, ATTACK, POST_ATTACK }
    private State currentState;

    public Tree(int x, int y) {
        super(x, y, 128, 64, new Hitbox(40, 20, 50, 44));
        this.currentState = State.IDLE;
        this.hasAttacked = false;

        Image idleSprite = ResourceManager.getImage("/resources/sprites/tree-idle.png");
        Image attackSprite = ResourceManager.getImage("/resources/sprites/tree-attack.png");

        if (idleSprite != null && attackSprite != null) {
            this.idleAnimator = new Animator(idleSprite, 64, 32, 18, 50, 0);
            this.attackAnimator = new Animator(attackSprite, 64, 32, 11, 80, 1);
            this.currentAnimator = idleAnimator;
        }
    }

    @Override
    public void update(double deltaTime, int currentSpeed) {
        x -= deltaTime * currentSpeed;

        if (currentAnimator != null) {
            currentAnimator.update();
        }

        switch (currentState) {
            case IDLE:
                // Ataca automáticamente cuando entra bien en la pantalla
                if (!hasAttacked && x < 650) {
                    setCurrentState(State.ATTACK);
                }
                break;
            case ATTACK:
                if (attackAnimator.getCurrentFrame() == PROJECTILE_LAUNCH_FRAME && !hasAttacked) {
                    // Preparamos el proyectil y lo guardamos
                    pendingProjectile = new WoodProyectile(this.x, this.y + 25);
                    hasAttacked = true;
                }
                if (attackAnimator.isAnimationComplete()) {
                    setCurrentState(State.POST_ATTACK);
                }
                break;
            case POST_ATTACK:
                break; // Se queda quieto hasta salir de la pantalla
        }
        hitbox.update(this.x, this.y);
    }

    // El EntityManager llamará a este método para extraer el proyectil
    @Override
    public AbstractEntity getSpawnedEntity() {
        AbstractEntity temp = pendingProjectile;
        pendingProjectile = null; // Lo vaciamos para no escupirlo dos veces
        return temp;
    }

    @Override
    public void draw(Graphics g) {
        if (currentAnimator != null) {
            currentAnimator.draw(g, x, y, width, height);
        } else {
            g.setColor(new Color(139, 69, 19)); // Color marrón
            g.fillRect(x, y, width, height);
        }
    }

    private void setCurrentState(State newState) {
        this.currentState = newState;
        if (newState == State.ATTACK) {
            currentAnimator = attackAnimator;
            currentAnimator.reset();
        } else if (newState == State.POST_ATTACK) {
            currentAnimator = idleAnimator;
            currentAnimator.reset();
        }
    }
}