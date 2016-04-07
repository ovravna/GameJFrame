package game.level.tiles;

//import java.util.*;

import java.util.Arrays;
import java.util.List;

public class BasicSolidTile extends BasicTile {

    public BasicSolidTile(int id, int x, int y, int tileColor, int levelColor) {
        this(id, x, y, tileColor, levelColor, Arrays.asList(0x000000));
    }

    public BasicSolidTile(int id, int x, int y, int tileColor, int levelColor, List<Integer> ignoreColors) {
        this(id, x, y, tileColor, levelColor, 8, ignoreColors);
    }

    public BasicSolidTile(int id, int x, int y, int tileColor, int levelColor, int block, List<Integer> ignoreColors) {
        super(id, x, y, tileColor, levelColor, block, ignoreColors);
        this.solid = true;
    }
}
