package game.level;

import game.InputHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static game.level.Levels.FIRST_LEVEL;

public class LevelManager {
    private final InputHandler input;
    List<Level> levels = new ArrayList<>();
    Levels currentLevel;
    Level gameLevel;



    public LevelManager(InputHandler input, Level... levels) {
        this.input = input;
        Arrays.asList(levels).forEach(level -> this.levels.add(level));
        currentLevel = FIRST_LEVEL;
        loadLevel(currentLevel);


    }

    private void loadLevel(Levels currentLevel) {
        switch (currentLevel) {
            case MENU:
                gameLevel = new Menu(input, "/levels/Black.png");
                break;
            case FIRST_LEVEL:
                gameLevel = levels.get(0);
        }

        gameLevel.loadLevel();
    }

    public void addLevel(Level... levels) {
        for (Level level : levels) {
            if (!(level == null || this.levels.contains(level))) {
                this.levels.add(level);
            }
        }


    }


    public Level currentLevel() {
        return gameLevel;
    }
}
