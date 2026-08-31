package game.main;

import java.awt.*;
import javax.swing.JPanel;
import game.UI.CharacterFrame;
import game.UI.Heart;
import game.entities.AbstractEntity;
import game.entities.character.Character;
import game.entities.terrain.Background;
import game.entities.terrain.Floor;
import game.handlers.KeyHandler;
import game.manager.CollisionManager;
import game.manager.EntityManager;
import game.manager.LivesManager;
import game.manager.ScoreManager;
import game.manager.SpeedManager;
import game.manager.ResourceManager;
import game.utils.SoundPlayer;
import game.utils.TextGenerator;
import game.states.GameState;
import game.states.IntroState;
import game.states.PlayingState;

public class GamePanel extends JPanel implements Runnable {

    // Componentes y Managers
    private Character character;
    private Floor floor;
    private Background background;
    private CharacterFrame characterFrame;
    private LivesManager livesManager;
    private ScoreManager scoreManager;
    private SpeedManager speedManager;
    private CollisionManager collisionManager;
    private EntityManager entityManager;
    private SoundPlayer soundPlayer;
    private KeyHandler keyHandler;

    // Estado del juego
    private GameState currentState;
    private boolean running;
    private Thread gameThread;

    // Variables de tiempo y configuración
    private double deltaTime;
    private long lastTime;
    private final int initialSpeed = 250;
    private final long speedIncreaseInterval = 15000;
    private Font customFont;
    private Font customBoldFont;

    public static boolean DEBUG_MODE = false;

    public GamePanel() {
        this.running = false;

        // Inicializar entidades y gestores
        this.character = new Character(this);
        this.floor = new Floor();
        this.background = new Background();
        this.characterFrame = new CharacterFrame();
        this.scoreManager = new ScoreManager();
        this.livesManager = new LivesManager();
        this.speedManager = new SpeedManager(initialSpeed, speedIncreaseInterval);
        this.soundPlayer = new SoundPlayer();
        this.collisionManager = new CollisionManager(soundPlayer, livesManager, scoreManager);
        this.entityManager = new EntityManager(this, character, speedManager);

        this.customFont = ResourceManager.getFont("/resources/font/AvenuePixel-Regular.ttf", 40f);
        this.customBoldFont = ResourceManager.getFont("/resources/font/AvenuePixelStroke-Regular.ttf", 40f);

        this.keyHandler = new KeyHandler(this);
        addKeyListener(keyHandler);
        setFocusable(true);

        this.lastTime = System.nanoTime();
        this.currentState = new IntroState(this); // Estado inicial
    }

    public void startGame() {
        if (!running) {
            this.running = true;
            if (gameThread == null) {
                gameThread = new Thread(this);
                gameThread.start();
                soundPlayer.setFile(SoundPlayer.Sound.MUSIC);
                soundPlayer.loop();
            }
        }
    }

    public void resetGame() {
        this.entityManager.resetEntities();
        this.speedManager.resetSpeed(initialSpeed);
        this.scoreManager.resetScore();
        this.livesManager.resetHearts();

        this.character = new Character(this);
        this.floor = new Floor();
        this.background = new Background();

        this.lastTime = System.nanoTime();
        changeState(new PlayingState(this));
    }

    @Override
    public void run() {
        final double TIME_PER_UPDATE = 1_000_000_000.0 / 60.0; // 60 Updates por segundo
        long previousTime = System.nanoTime();
        double accumulator = 0;

        while (running) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - previousTime;
            previousTime = currentTime;
            accumulator += elapsedTime;

            // Fijamos el deltaTime para que las físicas sean consistentes (1/60 de segundo)
            deltaTime = TIME_PER_UPDATE / 1_000_000_000.0;

            boolean shouldRender = false;

            // Actualizamos la lógica del juego en pasos fijos
            while (accumulator >= TIME_PER_UPDATE) {
                if (currentState != null) {
                    currentState.update();
                }
                accumulator -= TIME_PER_UPDATE;
                shouldRender = true; // Solo renderizamos si hubo una actualización de lógica
            }

            if (shouldRender) {
                repaint();
            } else {
                // Pequeño descanso de 1ms para no usar el 100% de la CPU mientras esperamos
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentState != null) {
            currentState.draw(g);
        }
    }

    // Método expuesto para dibujar los elementos genéricos en PLAYING o GAMEOVER
    public void drawGameElements(Graphics g) {
        background.draw(g, getWidth(), getHeight());
        floor.draw(g);
        character.draw(g);
        characterFrame.draw(g);

        if (DEBUG_MODE) character.getHitbox().draw(g);

        for (AbstractEntity entity : entityManager.getEntityList()) {
            entity.draw(g);
            if (DEBUG_MODE) entity.getHitbox().draw(g);
        }

        for (Heart heart : livesManager.getHearts()) {
            heart.draw(g);
        }

        // OPTIMIZACIÓN: Dibujar el puntaje directamente sin instanciar nuevos objetos
        String score = String.valueOf(scoreManager.getScore());
        g.setFont(customFont);
        g.setColor(Color.WHITE);
        FontMetrics metrics = g.getFontMetrics(customBoldFont);
        int posX = (getWidth() - metrics.stringWidth(score)) / 2;
        g.drawString(score, posX, 40);
    }

    // --- State Management ---
    public void changeState(GameState newState) {
        this.currentState = newState;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    // --- Getters para que los States accedan a la lógica ---
    public Character getCharacter() { return character; }
    public Background getGameBackground() { return background; }
    public Floor getFloor() { return floor; }
    public SpeedManager getSpeedManager() { return speedManager; }
    public ScoreManager getScoreManager() { return scoreManager; }
    public EntityManager getEntityManager() { return entityManager; }
    public CollisionManager getCollisionManager() { return collisionManager; }
    public LivesManager getLivesManager() { return livesManager; }
    public SoundPlayer getSoundPlayer() { return soundPlayer; }
    public double getDeltaTime() { return deltaTime; }
}