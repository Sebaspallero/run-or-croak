package game.manager;

import java.util.List;
import game.entities.AbstractEntity;
import game.entities.character.Character;
import game.entities.items.Cherry;
import game.entities.obstacles.AbstractObstacle;
import game.utils.SoundPlayer;
import game.utils.SoundPlayer.Sound;
import game.entities.items.HeartItem;

public class CollisionManager {
    private SoundPlayer soundPlayer;
    private LivesManager livesManager;
    private ScoreManager scoreManager;

    public CollisionManager(SoundPlayer soundPlayer, LivesManager livesManager, ScoreManager scoreManager) {
        this.soundPlayer = soundPlayer;
        this.livesManager = livesManager;
        this.scoreManager = scoreManager;
    }

    public void handleCollisions(Character character, List<AbstractEntity> entityList) {
        for (int i = 0; i < entityList.size(); i++) {
            AbstractEntity entity = entityList.get(i);

            if (entity instanceof AbstractObstacle) {
                if (checkCollision(character, entity)) {
                    // Si el personaje NO es invulnerable, le hacemos daño
                    if (!character.isInvulnerable()) {
                        handleCharacterCollision(character);
                    }
                }
                if (entity.isOutOfScreen()) {
                    entityList.remove(i);
                    i--;
                }
            } else if (entity instanceof Cherry) {
                if (checkCollision(character, entity)) {
                    soundPlayer.setFile(Sound.COIN);
                    soundPlayer.play();
                    scoreManager.addPoints(20);
                    entityList.remove(i);
                    i--;
                }
            } else if (entity instanceof HeartItem) {
                if (checkCollision(character, entity)) {
                    soundPlayer.setFile(Sound.COIN); // Usamos el sonido de moneda por ahora
                    soundPlayer.play();

                    // Si se curó da 50pts. Si ya estaba lleno de vida, ¡da 200pts de bonus!
                    if (livesManager.heal()) {
                        scoreManager.addPoints(50);
                    } else {
                        scoreManager.addPoints(200);
                    }

                    entityList.remove(i);
                    i--;
                }
            }
        }
    }

    private boolean checkCollision(Character character, AbstractEntity entity) {
        return character.getHitbox().intersects(entity.getHitbox());
    }

    private void handleCharacterCollision(Character character) {
        character.onCollision();
        soundPlayer.setFile(Sound.HURT); // Es mejor sonido para daño que Game Over
        soundPlayer.play();
        livesManager.updateHeart();
    }
}