package game.level;

import game.InputHandler;
import game.gfx.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.level.Levels.MENU;

public class LevelManager {
    private final Screen screen;
    private final InputHandler input;
    private List<Level> levels = new ArrayList<>();
    private Levels currentLevel;
    private Level gameLevel;



    public LevelManager(Screen screen, InputHandler input) {
        this.screen = screen;
        this.input = input;
        currentLevel = MENU;

        loadLevel(currentLevel);


    }

    public void loadLevel(Level level) {
        gameLevel = level;
        if (gameLevel.containsPlayer()) {
            gameLevel.player.setInputHandler(input);
        }

        gameLevel.loadLevel();
    }

    public void loadLevel(Levels currentLevel) {
        gameLevel = null;
        switch (currentLevel) {
            case MENU:
                gameLevel = new Menu(this, input, "/levels/Black.png");
                break;
            case FIRST_LEVEL:
                gameLevel = levels.get(0);
        }

        if (gameLevel.containsPlayer()) {
            gameLevel.player.setInputHandler(input);
        }

        gameLevel.loadLevel();
    }

    public void addLevel(Level level) {
        if (level != null && !levels.contains(level)) {
            levels.add(level);
        }
    }

    public void addLevels(Level... levels) {
        for (Level level : levels) {
            if (!(level == null || this.levels.contains(level))) {
                this.levels.add(level);
            }
        }
    }

    public Level currentLevel() {
        return gameLevel;
    }

    public void tick() {
        gameLevel.tick();

    }

    public void draw(Graphics g) {
        gameLevel.draw(g, screen);


    }
}
