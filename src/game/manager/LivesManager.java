package game.manager;

import java.util.ArrayList;
import java.util.List;

import game.UI.Heart;
import game.entities.character.Character;

public class LivesManager {

    private List<Heart> hearts = new ArrayList<>();
    private final int FIRST = 65;
    private final int SECOND = 97;
    private final int THIRD = 129;

    public LivesManager() {
        hearts.add(new Heart(FIRST, true));
        hearts.add(new Heart(SECOND, true));
        hearts.add(new Heart(THIRD, true));
    }

    public void updateHeart() {
        for (int i = 2; i >= 0; i--) {
            if (hearts.get(i).isFull()) {
                hearts.get(i).setFull(false);
                break;
            }
        }
    }

    public boolean heal() {
        for (int i = 0; i < hearts.size(); i++) {
            if (!hearts.get(i).isFull()) {
                hearts.get(i).setFull(true);
                return true; // Se curó un corazón
            }
        }
        return false; // Ya tenía la vida al máximo
    }

    public boolean checkHearts() {
        if (hearts.get(0).isFull()) {
            return false;
        } else {
            return true;
        }
    }

    public void resetHearts() {
        hearts.clear();

        hearts.add(new Heart(FIRST, true));
        hearts.add(new Heart(SECOND, true));
        hearts.add(new Heart(THIRD, true));
    }

    public List<Heart> getHearts() {
        return hearts;
    }

}
