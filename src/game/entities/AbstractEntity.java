package game.entities;

import java.awt.Graphics;

public abstract class AbstractEntity {
    protected int x, y;
    protected int width, height;
    protected Hitbox hitbox;
    protected State currentState;

    public enum State {
        IDLE,
        COLLECTED,
    }

    public AbstractEntity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitbox = new Hitbox(0, 0, width, height);
    }

    public abstract void update(double deltaTime, int currentSpeed);

    public abstract void draw(Graphics g);

    public Hitbox getHitbox() {
        return hitbox;
    }

    public boolean isOutOfScreen() {
        return x + width < 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
    }

    public AbstractEntity getSpawnedEntity() {
        return null; // Por defecto las entidades no generan otras entidades
    }
}