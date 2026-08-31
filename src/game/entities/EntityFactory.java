package game.entities;

import game.entities.items.Cherry;
import game.entities.items.HeartItem;
import game.entities.obstacles.*;

public class EntityFactory {
    public static Bird createBird(int x, int y) { return new Bird(x, y); }
    public static Pig createPig(int x, int y) { return new Pig(x, y); }
    public static Spike createSpike(int x, int y) { return new Spike(x, y); }
    public static Bee createBee(int x, int minY, int maxY) { return new Bee(x, minY, maxY); }
    public static Tree createTree(int x, int y) { return new Tree(x, y); }
    public static Cherry createCherry(int x, int y) { return new Cherry(x, y); }
    public static HeartItem createHeart(int x, int y) { return new HeartItem(x, y); }
}