package game.states;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import game.main.GamePanel;
import game.entities.character.Character;
import game.utils.SoundPlayer;

public class PlayingState implements GameState {
    private GamePanel gp;

    public PlayingState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void update() {
        Character character = gp.getCharacter();

        gp.getSpeedManager().update();
        character.update();
        gp.getScoreManager().update();
        gp.getFloor().update(gp.getDeltaTime(), gp.getSpeedManager().getCurrentSpeed());
        gp.getGameBackground().update(gp.getDeltaTime(), 100);
        gp.getEntityManager().update(gp.getDeltaTime(), gp.getSpeedManager().getCurrentSpeed(), character);
        gp.getCollisionManager().handleCollisions(character, gp.getEntityManager().getEntityList());

        if (gp.getLivesManager().checkHearts()) {
            gp.changeState(new GameOverState(gp));
        }
    }

    @Override
    public void draw(Graphics g) {
        gp.drawGameElements(g);
    }

    @Override
    public void keyPressed(int keyCode) {
        Character character = gp.getCharacter();

        // ACTIVAR MODO DEBUG CON F3
        if (keyCode == KeyEvent.VK_F3) {
            GamePanel.DEBUG_MODE = !GamePanel.DEBUG_MODE;
        }

        // SALTO
        if (keyCode == KeyEvent.VK_SPACE) {
            character.jump();
            gp.getSoundPlayer().setFile(SoundPlayer.Sound.JUMP);
            gp.getSoundPlayer().play();
        }

        // AGACHARSE
        if (keyCode == KeyEvent.VK_DOWN) {
            character.crouch();
        }

        // PAUSA (NUEVO)
        if (keyCode == KeyEvent.VK_P || keyCode == KeyEvent.VK_ESCAPE) {
            // Cambiamos al estado de pausa y le pasamos "this" (el PlayingState actual) para que no se pierda
            gp.changeState(new PauseState(gp, this));
        }
    }

    @Override
    public void keyReleased(int keyCode) {
        // SOLTAR FLECHA ABAJO: Levantarse
        if (keyCode == KeyEvent.VK_DOWN) {
            if (gp.getCharacter().getCurrentState() != Character.State.HIT) {
                gp.getCharacter().stand();
            }
        }

        // SOLTAR ESPACIO: Cortar el salto (NUEVO)
        if (keyCode == KeyEvent.VK_SPACE) {
            gp.getCharacter().stopJump();
        }
    }
}
