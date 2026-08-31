package game.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // Importación clave
import game.entities.AbstractEntity;
import game.entities.character.Character;
import game.main.GamePanel;

public class EntityManager {
    // Usamos CopyOnWriteArrayList para que Swing pueda dibujarla sin errores de concurrencia
    private List<AbstractEntity> entityList;
    private long lastEntityTime;
    private long initialEntityInterval;
    private SpeedManager speedManager;

    public EntityManager(GamePanel gp, Character character, SpeedManager speedManager) {
        this.entityList = new CopyOnWriteArrayList<>();
        this.lastEntityTime = System.currentTimeMillis();
        this.initialEntityInterval = 2000;
        this.speedManager = speedManager;
    }

    public void update(double deltaTime, int currentSpeed, Character character) {
        long currentTime = System.currentTimeMillis();
        long currentInterval = Math.max(900, initialEntityInterval - (currentSpeed));

        if (currentTime - lastEntityTime >= currentInterval) {
            boolean isHardMode = speedManager.isAtMaxSpeed();
            PatternManager.spawnRandomPattern(entityList, isHardMode);
            lastEntityTime = currentTime;
        }

        List<AbstractEntity> newEntities = new ArrayList<>();
        List<AbstractEntity> entitiesToRemove = new ArrayList<>(); // Lista temporal para borrar

        for (AbstractEntity entity : entityList) {
            entity.update(deltaTime, currentSpeed);

            // Si el Árbol escupió madera, la recogemos
            AbstractEntity spawned = entity.getSpawnedEntity();
            if (spawned != null) {
                newEntities.add(spawned);
            }

            // Si salió de la pantalla, la marcamos para borrar
            if (entity.isOutOfScreen()) {
                entitiesToRemove.add(entity);
            }
        }

        // Aplicamos los cambios al final para no romper la iteración y evitar crasheos
        entityList.removeAll(entitiesToRemove);
        entityList.addAll(newEntities);
    }

    public void resetEntities() {
        entityList.clear();
        this.lastEntityTime = System.currentTimeMillis();
    }

    public List<AbstractEntity> getEntityList() { return entityList; }
}