package game.manager;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ResourceManager {
    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, Font> fontCache = new HashMap<>();

    public static Image getImage(String path) {
        if (!imageCache.containsKey(path)) {
            try {
                Image img = ImageIO.read(ResourceManager.class.getResource(path));
                imageCache.put(path, img);
            } catch (Exception e) {
                System.err.println("Error loading image: " + path);
                e.printStackTrace();
                return null;
            }
        }
        return imageCache.get(path);
    }

    public static Font getFont(String path, float size) {
        String key = path + "_" + size;
        if (!fontCache.containsKey(key)) {
            try (InputStream is = ResourceManager.class.getResourceAsStream(path)) {
                if (is == null) throw new IOException("Font not found: " + path);
                Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
                fontCache.put(key, font);
            } catch (FontFormatException | IOException e) {
                System.err.println("Error loading font: " + path);
                e.printStackTrace();
                fontCache.put(key, new Font("Arial", Font.PLAIN, (int) size));
            }
        }
        return fontCache.get(key);
    }
}
