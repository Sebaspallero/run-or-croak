package game.main;

import java.awt.*;

import game.UI.Heart;
import game.entities.Background;
import game.entities.Dinosaur;
import game.entities.Floor;
import game.entities.Obstacle;
import game.handlers.KeyHandler;
import game.manager.CollissionManager;
import game.manager.LivesManager;
import game.manager.ObstacleManager;
import game.manager.ScoreManager;
import game.manager.SpeedManager;
import game.utils.FontLoader;
import game.utils.SoundPlayer;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    private Dinosaur dinosaur;
    private Floor floor;
    private Background background;

    private LivesManager livesManager;
    private ScoreManager scoreManager;
    private SpeedManager speedManager;
    private CollissionManager collissionManager;
    private ObstacleManager obstacleManager;

    private Font customBoldFont;
    private Font customFont;

    private long hitStartTime;
    private final long HIT_DURATION = 1000;

    private boolean running;
    private boolean gameOver;

    private KeyHandler keyHandler;
    private SoundPlayer soundPlayer;

    private int initialSpeed = 200;
    private long speedIncreaseInterval = 15000; //Every 10 seconds increase the speed

    private long initialObstacleInterval = 2000; //New obstacle every 2 seconds
    private long obstacleIncreaseInterval = 10000;
    
    private double deltaTime; 
    private long lastTime;

    public GamePanel() {
        this.running = false;

        this.dinosaur = new Dinosaur();
        this.floor = new Floor();
        this.background = new Background();

        this.scoreManager = new ScoreManager();
        this.livesManager = new LivesManager();
        this.speedManager = new SpeedManager(initialSpeed, speedIncreaseInterval);
        this.collissionManager = new CollissionManager();
        this.obstacleManager = new ObstacleManager(initialObstacleInterval, obstacleIncreaseInterval);
        
        this.customFont = FontLoader.loadFont("/resources/font/PixelOperator8.ttf", 16f);
        this.customBoldFont = FontLoader.loadFont("/resources/font/PixelOperator8-Bold.ttf", 24f);

        this.keyHandler = new KeyHandler(dinosaur,this);
        this.soundPlayer = new SoundPlayer();

        this.gameOver = false;

        addKeyListener(keyHandler);
        setFocusable(true);

        this.lastTime = System.nanoTime();
        
    }

    public void startGame() {
        if (!running) {
            this.running = true;
            new Thread(this).start();
    
            soundPlayer.setFile(1);
            soundPlayer.play();
        }
    }

    public void resetGame(){
        this.running = false;

        try {
            Thread.sleep(100);  
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.gameOver = false;
        
        this.obstacleManager.resetObstacles(); 
        this.initialObstacleInterval = 2000;
        this.speedManager.resetSpeed();
        this.scoreManager.resetScore();
        this.livesManager.resetHearts();
        this.dinosaur = new Dinosaur();  
        this.floor = new Floor();  
        this.background = new Background(); 
        
        this.keyHandler.setDinosaur(this.dinosaur);

        this.lastTime = System.nanoTime();  
        this.hitStartTime = 0;

        this.running = true;
        new Thread(this).start();
        repaint();
    }

    @Override
    public void run() {
        while (running) {
            updateGame();
            repaint();
            try {
                Thread.sleep(16);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        if (!running || gameOver) {
            return;
        }
    
        //Calculate Delta Time
        getDeltaTime();
        
        //Check state of dinosaur
        if (isDinosaurHit()) {
            return;
        }

        updateGameElements();
        handleCollisions();
        generateObstacles();
    }

    private void getDeltaTime(){
        long now = System.nanoTime();
        deltaTime = (now - lastTime) / 1000000000.0;
        lastTime = now;
    }

    private boolean isDinosaurHit(){
        if (dinosaur.getCurrentState() == Dinosaur.State.HIT) {
            dinosaur.update(); // Actualiza animación de HIT
            long elapsedTime = System.currentTimeMillis() - hitStartTime;
    
            if (elapsedTime >= HIT_DURATION) {
                gameOver = true;
            }
            return true; // Indica que el dinosaurio está en estado HIT
        }
        return false; // Continúa con la actualización normal
    }

    private void updateGameElements(){
        speedManager.update();
        dinosaur.update();
        scoreManager.update();
        floor.update(deltaTime, speedManager.getCurrentSpeed());
        obstacleManager.update(deltaTime, speedManager.getCurrentSpeed());
    }

    private void handleCollisions() {
        boolean collisionDetected = false;
    
        for (int i = 0; i < obstacleManager.getObstacleList().size(); i++) {
            Obstacle obstacle = obstacleManager.getObstacleList().get(i);
            obstacle.update(deltaTime, speedManager.getCurrentSpeed());
    
            if (collissionManager.checkCollision(dinosaur, obstacleManager.getObstacleList())) {
                collisionDetected = true;
    
                if (!dinosaur.hasCollided()) {
                    handleDinosaurCollision();
                }
                break; // Salimos del bucle si detectamos una colisión
            }
    
            if (obstacle.isOutOfScreen()) {
                obstacleManager.getObstacleList().remove(i);
                i--;
            }
        }
    
        if (!collisionDetected && dinosaur.hasCollided()) {
            dinosaur.setCollided(false); // Restablece el estado del dinosaurio
        }
    }

    private void handleDinosaurCollision() {
        dinosaur.onCollision();
        soundPlayer.setFile(2);
        soundPlayer.play();
        hitStartTime = System.currentTimeMillis();
        livesManager.updateHeart(dinosaur);
    }

    private void generateObstacles() {
        obstacleManager.update(deltaTime, speedManager.getCurrentSpeed());
    }
    
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        background.draw(g, getWidth(), getHeight());
        floor.draw(g);
        dinosaur.draw(g);

        for (Heart heart : livesManager.getHearts()) {
            heart.draw(g);
        }

        for (Obstacle obstacle : obstacleManager.getObstacleList()) {
            obstacle.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(customFont);
        g.drawString("Score: " + scoreManager.getScore(), 20, 20);

        if (gameOver) {
            g.setColor(Color.RED); 
            g.setFont(customBoldFont);

            FontMetrics metrics = g.getFontMetrics(customBoldFont);
            String gameOver = "GAME OVER";
            int gameOverX = (getWidth() - metrics.stringWidth(gameOver)) / 2;
            int gameOverY = getHeight() / 2 - 20;
            g.drawString(gameOver, gameOverX, gameOverY);

           
            g.setFont(customFont);
            metrics = g.getFontMetrics(customFont);

            String scoreText = "Your score is: " + scoreManager.getScore();
            int scoreX = (getWidth() - metrics.stringWidth(scoreText)) / 2;
            int scoreY = gameOverY + 30;
            g.drawString(scoreText, scoreX, scoreY);
            

            String restartText = "Press enter to play again!";
            int restartX = (getWidth() - metrics.stringWidth(restartText)) / 2;
            int restartY = scoreY + 30;
            g.drawString(restartText, restartX, restartY);
        }
    }
}
