package sokoban.cells;

import game.entities.Mob;
import game.gfx.Screen;
import game.level.Level;

import java.util.Arrays;

public class Wall extends Mob {

    public Wall(Level level, int x, int y) {
        super(level, "Wall", x, y, 0);

        solid = true;
        pushable = false;
        dimentions = new int[] {16, 0, 16, 0};

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Screen screen) {
        screen.render(x, y, 10+5*(screen.width >> 4), 0, 1, 16, Arrays.asList());

    }

    @Override
    public void isPushed(int x, int y) {

    }
}
