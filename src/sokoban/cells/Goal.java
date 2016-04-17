package sokoban.cells;

import game.entities.Entity;
import game.entities.Mob;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Goal extends Mob {
    public static List<Goal> goals = new ArrayList<>();

    private final SpriteSheet goalSheet;
    private boolean isFilled;
    private boolean isWon;

    public Goal(Level level, int x, int y) {
        super(level, "Goal", x, y, 0);
        goalSheet = new SpriteSheet("/box16x16.png");

        solid = false;
        pushable = false;
        dimentions = new int[]{10, 6, 10, 6};
        goals.add(this);

    }

    public void setFilled() {
        isFilled = true;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public boolean isWon() {
        return isWon;
    }

    @Override
    public void tick() {
        if (isFilled && goals.stream().allMatch(Goal::isFilled) && !isWon) {
            isWon = true;

            for (Entity entity : level.entities) {
                if (entity instanceof Wall) {
                    entity.changePosition(0,-400);
                }
            }

//            removeEntity(250, 213);
        }


        isFilled = false;
    }

    @Override
    public void render(Screen screen) {
        screen.render(x, y, goalSheet, 0, 0, 1, 16, Arrays.asList(0xffffff));
    }

    @Override
    public void isPushed(int x, int y) {

    }
}
