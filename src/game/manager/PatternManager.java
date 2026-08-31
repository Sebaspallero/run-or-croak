package game.manager;

import game.entities.AbstractEntity;
import game.entities.EntityFactory;
import java.util.List;
import java.util.Random;

public class PatternManager {
    private static final Random RANDOM = new Random();
    private static final int SPAWN_X = 800; // Aparecen justo fuera de la pantalla a la derecha

    // Alturas predefinidas para el diseño de niveles
    private static final int GROUND_Y = 210;
    private static final int SPIKE_Y = 224; // Los pinchos son más bajitos, necesitan ir más abajo
    private static final int CROUCH_Y = 180; // A esta altura, te golpea si vas corriendo, te obliga a agacharte
    private static final int AIR_Y = 130; // Altura estándar para un salto normal
    private static final int HIGH_AIR_Y = 70; // Requiere un salto desde el punto más alto

    public static void spawnRandomPattern(List<AbstractEntity> entities, boolean isHardMode) {

        // 1. Tira los dados para el corazón: 5% de probabilidad de aparición
        if (RANDOM.nextInt(100) < 5) {
            spawnHeartAndThreat(entities);
            return; // Salimos para que esta oleada sea SÓLO la del corazón
        }

        // 2. Si no hubo suerte con el corazón, generamos un patrón de obstáculos
        // Asumiendo 7 patrones normales (0-6) y 4 difíciles adicionales (7-10)
        int maxPatterns = isHardMode ? 12 : 8;
        int pattern = RANDOM.nextInt(maxPatterns);

        switch (pattern) {
            // --- PATRONES NORMALES ---
            case 0: spawnSinglePig(entities); break;
            case 1: spawnSingleSpike(entities); break;
            case 2: spawnCherryLine(entities); break;
            case 3: spawnBirdAndCherries(entities); break;
            case 4: spawnCherryArc(entities); break;
            case 5: spawnMultiplePigs(entities); break;
            case 6: spawnTreeObstacle(entities); break;
            case 7: spawnDoubleSpike(entities); break; // <-- El árbol es un patrón normal

            // --- PATRONES DIFÍCILES (Solo a máxima velocidad) ---
            case 8: spawnSpikeAndBird(entities); break;
            case 9: spawnDoubleThreat(entities); break;
            case 10: spawnPigCherryPig(entities); break;
            case 11: spawnLowBirdWave(entities); break;
        }
    }

    private static void spawnSinglePig(List<AbstractEntity> entities) {
        entities.add(EntityFactory.createPig(SPAWN_X, GROUND_Y));
    }

    private static void spawnSingleSpike(List<AbstractEntity> entities) {
        entities.add(EntityFactory.createSpike(SPAWN_X, SPIKE_Y));
    }

    private static void spawnDoubleSpike(List<AbstractEntity> entities) {
        entities.add(EntityFactory.createSpike(SPAWN_X, SPIKE_Y));
        entities.add(EntityFactory.createSpike(SPAWN_X + 48, SPIKE_Y));
    }

    private static void spawnCherryLine(List<AbstractEntity> entities) {
        // De 3 a 5 cerezas en línea recta
        int randomAmount = RANDOM.nextInt(3) + 3;
        for (int i = 0; i < randomAmount; i++) {
            entities.add(EntityFactory.createCherry(SPAWN_X + (i * 50), GROUND_Y));
        }
    }

    private static void spawnBirdAndCherries(List<AbstractEntity> entities) {
        // Cereza antes del pájaro
        entities.add(EntityFactory.createCherry(SPAWN_X, GROUND_Y));

        // Pájaro en el medio obligando a agacharse (CROUCH_Y = 165 o 170 es ideal)
        entities.add(EntityFactory.createBird(SPAWN_X + 60, CROUCH_Y));

        // Cereza después del pájaro
        entities.add(EntityFactory.createCherry(SPAWN_X + 120, GROUND_Y));
    }

    private static void spawnCherryArc(List<AbstractEntity> entities) {
        entities.add(EntityFactory.createCherry(SPAWN_X, GROUND_Y));
        entities.add(EntityFactory.createCherry(SPAWN_X + 50, AIR_Y));
        entities.add(EntityFactory.createCherry(SPAWN_X + 100, HIGH_AIR_Y));
        entities.add(EntityFactory.createCherry(SPAWN_X + 150, AIR_Y));
        entities.add(EntityFactory.createCherry(SPAWN_X + 200, GROUND_Y));

        // Trampa debajo del arco para forzar el salto perfecto
        entities.add(EntityFactory.createSpike(SPAWN_X + 100, SPIKE_Y));
    }

    private static void spawnMultiplePigs(List<AbstractEntity> entities) {
        // 2 o 3 cerdos separados para saltar rítmicamente entre ellos
        int amount = RANDOM.nextBoolean() ? 2 : 3;
        for (int i = 0; i < amount; i++) {
            // Espacio de 180px entre cada cerdo para que el jugador tenga tiempo de caer y volver a saltar
            entities.add(EntityFactory.createPig(SPAWN_X + (i * 180), GROUND_Y));
        }
    }

    private static void spawnSpikeAndBird(List<AbstractEntity> entities) {
        // Pinchos en el suelo y pájaro en el aire (Deja un hueco muy preciso para saltar)
        entities.add(EntityFactory.createSpike(SPAWN_X, SPIKE_Y));
        entities.add(EntityFactory.createBird(SPAWN_X + 30, AIR_Y - 10));
    }

    private static void spawnDoubleThreat(List<AbstractEntity> entities) {
        // Un cerdo terrestre seguido rápidamente por una abeja impredecible
        entities.add(EntityFactory.createPig(SPAWN_X, GROUND_Y));
        entities.add(EntityFactory.createBee(SPAWN_X + 180, 50, 180));
    }

    private static void spawnPigCherryPig(List<AbstractEntity> entities) {
        // Cerdo -> Cereza en el aire -> Cerdo
        entities.add(EntityFactory.createPig(SPAWN_X, GROUND_Y));
        entities.add(EntityFactory.createCherry(SPAWN_X + 120, AIR_Y));
        entities.add(EntityFactory.createPig(SPAWN_X + 240, GROUND_Y));
    }

    private static void spawnLowBirdWave(List<AbstractEntity> entities) {
        // Pasillo de pájaros bajos que fuerzan a mantener la tecla hacia abajo apretada más tiempo
        entities.add(EntityFactory.createBird(SPAWN_X, CROUCH_Y));
        entities.add(EntityFactory.createCherry(SPAWN_X + 80, GROUND_Y));
        entities.add(EntityFactory.createBird(SPAWN_X + 160, CROUCH_Y));
    }

    private static void spawnTreeObstacle(List<AbstractEntity> entities) {
        // Un árbol solitario que te disparará un proyectil rápido
        entities.add(EntityFactory.createTree(SPAWN_X, GROUND_Y));
    }

    private static void spawnHeartAndThreat(List<AbstractEntity> entities) {
        // Te tienta con un corazón en el aire, pero hay un cerdo abajo
        entities.add(EntityFactory.createHeart(SPAWN_X, AIR_Y));
        entities.add(EntityFactory.createPig(SPAWN_X, GROUND_Y));
    }
}