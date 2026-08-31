package game.states;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.main.GamePanel;
import game.UI.IntroScreen;
import game.manager.ResourceManager;

public class IntroState implements GameState {
    private GamePanel gp;
    private long introStartTime;
    private Font customBoldFont;

    public IntroState(GamePanel gp) {
        this.gp = gp;
        this.introStartTime = System.currentTimeMillis();
        this.customBoldFont = ResourceManager.getFont("/resources/font/AvenuePixelStroke-Regular.ttf", 40f);
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - introStartTime >= 5000) {
            gp.changeState(new TitleState(gp));
        }
    }

    @Override
    public void draw(Graphics g) {
        IntroScreen.drawIntroScreen(g, customBoldFont, gp.getWidth(), gp.getHeight());
    }

    @Override
    public void keyPressed(int keyCode) {
        // Permitir saltar la intro con Enter o Espacio
        if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
            gp.changeState(new TitleState(gp));
        }
    }

    @Override
    public void keyReleased(int keyCode) {}
}
