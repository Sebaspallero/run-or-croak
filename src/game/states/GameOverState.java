package game.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.main.GamePanel;
import game.UI.GameOverScreen;

public class GameOverState implements GameState {
    private GamePanel gp;

    public GameOverState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void update() {
        // En Game Over detenemos las actualizaciones para congelar la pantalla
    }

    @Override
    public void draw(Graphics g) {
        gp.drawGameElements(g); // Dibujamos el fondo congelado
        GameOverScreen.gameOverScreen(g, gp.getWidth(), gp.getHeight(), gp.getScoreManager().getScore(), gp);
    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER) {
            gp.resetGame();
        }
    }

    @Override
    public void keyReleased(int keyCode) {}
}