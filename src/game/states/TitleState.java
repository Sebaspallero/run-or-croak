package game.states;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.main.GamePanel;
import game.UI.TitleScreen;
import game.manager.ResourceManager;

public class TitleState implements GameState {
    private GamePanel gp;
    private Font customFont;

    public TitleState(GamePanel gp) {
        this.gp = gp;
        this.customFont = ResourceManager.getFont("/resources/font/AvenuePixel-Regular.ttf", 40f);
    }

    @Override
    public void update() {
        gp.getCharacter().update();
        gp.getGameBackground().update(gp.getDeltaTime(), 100);
    }

    @Override
    public void draw(Graphics g) {
        TitleScreen.drawTitleScreen(g, customFont, gp.getWidth(), gp.getHeight(), gp, gp.getGameBackground(), gp.getFloor(), gp.getCharacter());
    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            gp.changeState(new PlayingState(gp));
        }
    }

    @Override
    public void keyReleased(int keyCode) {}
}
