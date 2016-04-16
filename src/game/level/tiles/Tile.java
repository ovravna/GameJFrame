package game.level.tiles;

//import java.util.*;

import game.gfx.Screen;
import game.level.Level;

import java.util.Arrays;
import java.util.List;

public abstract class Tile {


    public static final Tile[] tiles = new Tile[256];
    public static final Tile VOID = new BasicSolidTile(0, 0, 0,  0xff000000);
    public static final Tile STONE = new BasicSolidTile(1, 1, 0,  0xff555555);
    public static final Tile GRASS = new BasicTile(2, 2, 0,  0xff00ff00);
    public static final Tile WATER = new AnimatedTile(3, new int[][]{{3, 0}, {4, 0}, {5, 0}, {4, 0}}, 0xff0000ff, 700);
    public static final Tile SAND = new BasicTile(4, 7, 0, 0xffDD8800);
    public static final Tile ROCK1 = new BasicSolidTile(11, 1, 7,
            0xff111111, 16, Arrays.asList(0xffffff));
    public static final Tile BLACK = new BasicSolidTile(5, 0, 0, 0x000000);



    protected byte id;
    protected boolean solid;
    protected boolean emitter;
    private int levelColor;
    protected int screenBlocks = 16;
    protected List<Integer> ignoreColors;


    public Tile(int id, boolean solid, boolean emitter, int levelColor, List<Integer> ignoreColors) {
        this.id = (byte) id;
        if (tiles[id] != null) throw new RuntimeException("Duplecate tile-ID on"+id);
        this.solid = solid;
        this.emitter = emitter;
        this.levelColor = levelColor;
        this.ignoreColors = ignoreColors;
        tiles[id] = this;
    }

    public byte getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isEmitter() {
        return emitter;
    }

    public abstract void tick();

    public abstract void render(Screen screen, Level level, int x, int y);

    public int getLevelColor() {
        return levelColor;
    }

}
