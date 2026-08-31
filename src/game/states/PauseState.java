package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.main.GamePanel;
import game.manager.ResourceManager;

public class PauseState implements GameState {
    private GamePanel gp;
    private GameState previousState; // Guardamos el estado de juego activo
    private Font customBoldFont;

    // El constructor recibe el GamePanel y el estado que estaba corriendo (PlayingState)
    public PauseState(GamePanel gp, GameState previousState) {
        this.gp = gp;
        this.previousState = previousState;
        this.customBoldFont = ResourceManager.getFont("/resources/font/AvenuePixelStroke-Regular.ttf", 60f);
    }

    @Override
    public void update() {
        // El juego está pausado, ¡NO llamamos al update del previousState!
        // Así congelamos el tiempo y las físicas por completo.
    }

    @Override
    public void draw(Graphics g) {
        // 1. Dibujamos el juego congelado de fondo
        previousState.draw(g);

        // 2. Dibujamos un rectángulo negro semitransparente encima (Efecto oscurecido)
        g.setColor(new Color(0, 0, 0, 150)); // El 150 es el canal Alpha (Transparencia)
        g.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        // 3. Dibujamos el texto de PAUSA en el centro
        String pauseText = "PAUSADO";
        g.setFont(customBoldFont);
        FontMetrics metrics = g.getFontMetrics(customBoldFont);
        int x = (gp.getWidth() - metrics.stringWidth(pauseText)) / 2;
        int y = gp.getHeight() / 2;

        g.setColor(Color.WHITE);
        g.drawString(pauseText, x, y);
    }

    @Override
    public void keyPressed(int keyCode) {
        // Si volvemos a presionar P o ESC, restauramos el estado anterior para seguir jugando
        if (keyCode == KeyEvent.VK_P || keyCode == KeyEvent.VK_ESCAPE) {
            gp.changeState(previousState);
        }
    }

    @Override
    public void keyReleased(int keyCode) {}
}
