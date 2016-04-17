package game.level;

//import java.util.*;

import game.entities.Entity;
import game.entities.Player;
import game.gfx.Screen;
import game.level.tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Level {

    private byte[] tiles;
    public int width;
    public int height;
    public List<Entity> entities = new ArrayList<>();
    private String imagePath;
    private BufferedImage image;
    public Player player;
    public static HashMap<Integer, Tile> tileColors = new HashMap<>();


    public Level(String imagePath) {
//        System.out.println(imagePath);
        this.imagePath = imagePath;

        if (imagePath != null) {
            loadImage();
        } else {
            this.width = 64;
            this.height = 64;
            tiles = new byte[width*height];
            this.generateLevel();
        }
    }

    private void loadImage() {
        try {
            this.image = ImageIO.read(Level.class.getResource(this.imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLevel() {

        tiles = new byte[width*height];
        this.loadTiles();
    }

    private void loadTiles() {
        int[] tileColor = this.image.getRGB(0, 0, width, height, null, 0, width);

        for (int y = 0;y < height;y++) {
            for (int x = 0;x < width;x++) {
                if (tileColors.containsKey(tileColor[x+y*width])) {
                    this.tiles[x+y*width] = tileColors.get(tileColor[x+y*width]).getId();
                }
            }
        }
    }



    private void saveLevelToFile() {

        try {
            ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alterTile(int x, int y, Tile newTile) {
        this.tiles[x+y*width] = newTile.getId();
        image.setRGB(x, y, newTile.getLevelColor());
    }

    public void generateLevel() {
        for (int y = 0;y < height;y++) {
            for (int x = 0;x < width;x++) {
                if (x * y % 10 < 7) {
                    tiles[x+y*width] = Tile.GRASS.getId();
                } else {
                    tiles[x+y*width] = Tile.STONE.getId();
                }
            }
        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0) {
            xOffset = 0;
        }
        if (xOffset > (width << 3)- screen.width) {
            xOffset = (width << 3)-screen.width;
        }
        if (yOffset < 0) {
            yOffset = 0;
        }
        if (yOffset > (height << 3)-screen.height) {
            yOffset = (height << 3)-screen.height;
        }

        screen.setOffset(xOffset, yOffset);

        for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++) {
			for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
				getTile(x, y).render(screen, this, x << 3, y << 3);
            }
		}




//        for (int y = 0;y < height;y++) {
//            for (int x = 0;x < width;x++) {
//                getTile(x, y).render(screen, this, x << 3, y << 3);
//            }
//        }
    }

//    n instanceof Player?1:-1

    public void renderEntities(Screen screen) {
        entities.sort((n,m) -> n.compareTo(m));
        entities.forEach(entity -> entity.render(screen));
    }


    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return Tile.VOID;
        return Tile.tiles[tiles[x+y*width]];
    }

    public Entity getEntity(int x, int y) {
        List<Entity> entities = this.entities.stream().
                filter(n -> n.entityOn(x, y)).collect(Collectors.toList());
        if (entities.size() > 0) {
            return entities.get(0);
        }
        return null;
    }

    public void tick() {
        entities.forEach(Entity::tick);
//        System.out.println(entities.size());
        for (Tile t : Tile.tiles) {
            if (t == null) break;
            t.tick();
        }

    }

    public boolean containsPlayer() {
        return player != null;
    }

    public void addEntities(Entity entity) {
        if (entity instanceof Player) this.player = (Player) entity;
        this.entities.add(entity);

    }

    public void draw(Graphics g, Screen screen) {

    }
}
