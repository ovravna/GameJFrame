package sokoban.cells;

import game.entities.Mob;
import game.gfx.Colors;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

public class Box extends Mob{

    private int color = Colors.get(432, 543, 432, 000);
    private SpriteSheet sheet = new SpriteSheet("/wall16x16.png");


    public Box(Level level, String name, int x, int y) {
        super(level, name, x, y, 1);
        solid = true;
        pushable = true;
        dimentions = new int[]{16, 0, 16, 0};

    }


    @Override
    public void tick() {
        if (level.player == null) return;

//        if (level.player )

    }

    @Override
    public void render(Screen screen) {
        screen.render(x, y, sheet,0, 0, 1, 16);

    }

    @Override
    public void isPushed(int x, int y) {
        move(x, y);

    }

    @Override
    protected boolean hasColided(int xa, int ya) {
        return false;
    }
}