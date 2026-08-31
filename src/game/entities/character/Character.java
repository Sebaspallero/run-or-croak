package game.entities.character;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import game.entities.Hitbox;
import game.main.GamePanel;
import game.states.TitleState;
import game.utils.Animator;
import game.manager.ResourceManager;

public class Character {
    public enum State { RUNNING, CROUCHING, HIT, JUMPING, FALLING, IDLE }

    private static final int GROUND_Y = 210;
    private static final double GRAVITY = 1;
    private static final int JUMP_STRENGTH = -17;
    private static final int HITBOX_OFFSET_X = 15;
    private static final long INVULNERABILITY_DURATION = 1500; // 1.5 segundos de invulnerabilidad

    private int x, y, width, height, velocityY;
    private boolean jumping, crouching;

    // Variables de daño
    private boolean isInvulnerable;
    private long hitStartTime;

    private State currentState;
    private Hitbox hitbox;
    private Map<State, Animator> animations;
    private GamePanel gamePanel;

    public Character(GamePanel gp) {
        this.x = 50;
        this.y = GROUND_Y;
        this.width = 64;
        this.height = 64;
        this.velocityY = 0;
        this.jumping = false;
        this.crouching = false;
        this.isInvulnerable = false;
        this.currentState = State.RUNNING;
        this.gamePanel = gp;
        this.animations = new HashMap<>();
        this.hitbox = new Hitbox(15, 15, 32, 38);
        initializeAnimations();
    }

    public void initializeAnimations() {
        animations.put(State.RUNNING, createAnimator("/resources/sprites/frog-run.png", 12, 80));
        animations.put(State.HIT, createAnimator("/resources/sprites/frog-hit.png", 7, 80));
        animations.put(State.JUMPING, createAnimator("/resources/sprites/frog-jump.png", 1, 80));
        animations.put(State.FALLING, createAnimator("/resources/sprites/frog-fall.png", 1, 80));
        animations.put(State.CROUCHING, createAnimator("/resources/sprites/frog-roll.png", 5, 80));
        animations.put(State.IDLE, createAnimator("/resources/sprites/frog-idle.png", 5, 100));
    }

    public Animator createAnimator(String path, int frameCount, int frameDelay) {
        Image spriteSheet = ResourceManager.getImage(path);
        return new Animator(spriteSheet, 64, 64, frameCount, frameDelay, 0);
    }

    public void update() {
        // 1. APLICAR FÍSICAS SIEMPRE (incluso si está golpeado)
        if (jumping || y < GROUND_Y) {
            handleJump();
        }

        // 2. ACTUALIZAR INVULNERABILIDAD
        if (isInvulnerable) {
            if (System.currentTimeMillis() - hitStartTime >= INVULNERABILITY_DURATION) {
                isInvulnerable = false;
            }
        }

        // 3. DETERMINAR ANIMACIÓN (ESTADO)
        if (gamePanel.getCurrentState() instanceof TitleState) {
            currentState = State.IDLE;
        } else if (isInvulnerable && (System.currentTimeMillis() - hitStartTime < 400)) {
            // Mostrar animación de recibir daño solo los primeros 400ms
            currentState = State.HIT;
        } else {
            if (jumping || y < GROUND_Y) {
                currentState = (velocityY < 0) ? State.JUMPING : State.FALLING;
            } else if (crouching) {
                currentState = State.CROUCHING;
            } else {
                currentState = State.RUNNING;
            }
        }

        animations.get(currentState).update();
        updateHitbox();
    }

    private void handleJump() {
        velocityY += GRAVITY;
        y += velocityY;
        if (y >= GROUND_Y) {
            resetToGround();
        }
    }

    private void resetToGround() {
        y = GROUND_Y;
        jumping = false;
        velocityY = 0;
    }

    private void updateHitbox() {
        if (currentState == State.CROUCHING) {
            // La caja se hace más bajita y el margen superior aumenta para pegarse al suelo
            hitbox.setOffsets(15, 30);
            hitbox.setDimensions(32, 23);
        } else {
            // Caja normal para correr y saltar
            hitbox.setOffsets(15, 15);
            hitbox.setDimensions(32, 38);
        }

        // Finalmente, le pasamos la X e Y reales del jugador para que calcule su posición final
        hitbox.update(this.x, this.y);
    }

    public void jump() {
        if (!jumping) {
            jumping = true;
            velocityY = JUMP_STRENGTH;
        }
    }

    public void stopJump() {
        // Si estamos saltando y todavía estamos yendo hacia arriba (velocidad negativa)
        if (jumping && velocityY < 0) {
            velocityY /= 2; // Cortamos la fuerza ascendente a la mitad
        }
    }

    public void crouch() {
        if (!crouching) crouching = true;
    }

    public void stand() {
        if (crouching) crouching = false;
    }

    public void onCollision() {
        if (isInvulnerable) return; // Evita recibir varios golpes seguidos

        this.isInvulnerable = true;
        this.hitStartTime = System.currentTimeMillis();
        this.crouching = false;
        // Físicas eliminadas de aquí para no frenar el salto
    }

    public void draw(Graphics g) {
        // Efecto de parpadeo visual (Blink) si es invulnerable
        if (isInvulnerable) {
            // No dibuja el personaje 1 de cada 2 décimas de segundo
            if ((System.currentTimeMillis() / 100) % 2 == 0) {
                return;
            }
        }

        try {
            animations.get(currentState).draw(g, x, y, width, height);
        } catch (Exception e) {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
        }
    }

    public Hitbox getHitbox() { return this.hitbox; }
    public State getCurrentState() { return this.currentState; }
    public boolean isInvulnerable() { return this.isInvulnerable; }
    public int getCharacterY() { return this.y; }
}