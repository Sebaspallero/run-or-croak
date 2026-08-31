package game.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import game.main.GamePanel;
import game.utils.TextGenerator;
import game.manager.ResourceManager;

public class GameOverScreen {

    public static void gameOverScreen(Graphics g, int width, int height, int score, GamePanel gp) {
        Font customFont = ResourceManager.getFont("/resources/font/AvenuePixel-Regular.ttf", 40f);
        Font customBoldFont = ResourceManager.getFont("/resources/font/AvenuePixelStroke-Regular.ttf", 70f);

        FontMetrics metrics = g.getFontMetrics(customBoldFont);

        g.setColor(new Color(33, 31, 48));
        g.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        String gameOver = "GAME OVER";
        int gameOverX = (width - metrics.stringWidth(gameOver)) / 2;
        int gameOverY = height / 2 - 20;
        TextGenerator gameOverText = new TextGenerator(gameOver, gameOverX, gameOverY, customBoldFont,
                new Color(204, 48, 72));
        gameOverText.draw(g);

        metrics = g.getFontMetrics(customFont);

        String scoreText = "Your score is: " + score;
        int scoreX = (width - metrics.stringWidth(scoreText)) / 2;
        int scoreY = gameOverY + 50;
        TextGenerator scText = new TextGenerator(scoreText, scoreX, scoreY, customFont, new Color(255, 255, 255));
        scText.draw(g);

        String restart = "Press ENTER to play again!";
        int restartX = (width - metrics.stringWidth(restart)) / 2;
        int restartY = scoreY + 50;
        TextGenerator restartText = new TextGenerator(restart, restartX, restartY, customFont,
                new Color(255, 255, 255));
        restartText.draw(g);
    }

}
