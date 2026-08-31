package game.states;

import java.awt.Graphics;

public interface GameState {
    void update();
    void draw(Graphics g);
    void keyPressed(int keyCode);
    void keyReleased(int keyCode);
}
