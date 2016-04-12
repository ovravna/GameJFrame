package game.level.tiles;

import game.gfx.Screen;
import game.level.Level;

import java.util.Arrays;
import java.util.List;

public class BasicTile extends Tile {

    protected int tileId;
    protected int tileColor;
    protected int block;


    public BasicTile(int id, int x, int y, int tileColor, int levelColor) {
        this(id, x, y, tileColor, levelColor, Arrays.asList(0x000000));
    }

    public BasicTile(int id, int x, int y, int tileColor, int levelColor, List<Integer> ignoreColors) {
        this(id, x, y, tileColor, levelColor, 8, ignoreColors);
    }

    public BasicTile(int id, int x, int y, int tileColor, int levelColor, int block, List<Integer> ignoreColors) {
        super(id, false, false, levelColor, ignoreColors);
        this.tileId = x+y*(128/block);
        this.tileColor = tileColor;
        this.block = block;
    }


    @Override
    public void tick() {

    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tileId, 0, 1, block, Arrays.asList(0x000000));
    }
}