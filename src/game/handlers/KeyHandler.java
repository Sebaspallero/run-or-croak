package game.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import game.main.GamePanel;

public class KeyHandler implements KeyListener {
    private GamePanel gp;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (gp.getCurrentState() != null) {
            gp.getCurrentState().keyPressed(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gp.getCurrentState() != null) {
            gp.getCurrentState().keyReleased(e.getKeyCode());
        }
    }
}