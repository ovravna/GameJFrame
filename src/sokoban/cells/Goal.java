package sokoban.cells;

import game.entities.Mob;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

import java.util.Arrays;

public class Goal extends Mob {

    private final SpriteSheet goalSheet;

    public Goal(Level level, int x, int y) {
        super(level, "Goal", x, y, 0);
        goalSheet = new SpriteSheet("/box16x16.png");

        solid = false;
        pushable = false;
        dimentions = new int[]{10, 6, 10, 6};

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Screen screen) {
        screen.render(x, y, goalSheet, 0, 0, 1, 16, Arrays.asList(0xffffff));


    }

    @Override
    public void isPushed(int x, int y) {

    }
}
