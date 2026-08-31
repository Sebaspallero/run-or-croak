package game.entities;

import java.awt.*;

public class Hitbox {
    private int x, y;
    private int offsetX, offsetY; // Distancia relativa desde la X e Y de la entidad
    private int width, height;

    public Hitbox(int offsetX, int offsetY, int width, int height) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    // Ahora solo le pasamos las coordenadas de la entidad padre
    public void update(int parentX, int parentY) {
        this.x = parentX + offsetX;
        this.y = parentY + offsetY;
    }

    public boolean intersects(Hitbox other) {
        return this.toRectangle().intersects(other.toRectangle());
    }

    public Rectangle toRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        // Cajas rojas semitransparentes
        g.setColor(new Color(255, 0, 0, 100));
        g.fillRect(x, y, width, height);
        g.setColor(Color.RED);
        g.drawRect(x, y, width, height);
    }

    public void setOffsets(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }
}