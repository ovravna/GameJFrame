package game.level;

import game.InputHandler;
import game.InputManager;
import game.entities.Player;
import game.gfx.Screen;
import sokoban.Sokoban;
import sokoban.cells.Goal;
import sokoban.cells.Lantern;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.level.Levels.MENU;

public class LevelManager extends InputManager {
    public final Screen screen;
    private List<Level> levels = new ArrayList<>();
    private Levels currentLevel;
    private Level gameLevel;
    private int smoothRise;
    private int cycleTime = 20;
    private boolean wonFlag;


    public LevelManager(Screen screen, InputHandler input) {
        this.screen = screen;

        super.setInput(input);
        currentLevel = MENU;
        loadLevel(currentLevel);
    }

    public void loadLevel(Level level) {
        gameLevel = level;

        if (gameLevel.levelManager == null) {
            gameLevel.addManager(this);
        }

        gameLevel.loadLevel();
    }

    public void loadLevel(Levels currentLevel) {
        gameLevel = null;
        switch (currentLevel) {
            case MENU:
            default:
                gameLevel = new Menu(this, "/levels/Black.png");
                break;
            case FIRST_LEVEL:
                gameLevel = levels.get(0);
                gameLevel.addManager(this);
//                gameLevel.renderLight(false);
                break;
            case TEST:
                Level level = new Level(this, Sokoban.LEVEL);
                new Player(level, 0, 0);
//                new Lantern(level, 20, 10, -0xaa);
                new Lantern(level, 30, 10, -0x11);

//                new LightPoint(level, 40, 40, -0x661100, 20);
                loadLevel(level);
                break;
            case NEXT:
                loadLevel(Sokoban.init(Sokoban.nextBoard()));
                break;
        }

        gameLevel.loadLevel();
    }

    public void addLevel(Level level) {
        if (level != null && !levels.contains(level)) {
            levels.add(level);
            level.addManager(this);
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


        if (!wonFlag && gameLevel.entities.stream().anyMatch(n -> n instanceof Goal && ((Goal) n).isWon())) {
            smoothRise = 1;
            cycleTime = 1;
            wonFlag = true;
        }

        if (smoothRise > 0) {
            if (gameLevel.lighting.setFilter(cycleTime, smoothRise == 1 ? true:false, 0xff)) {
                smoothRise = 0;
            }
        }

        if (wonFlag && smoothRise == 0) {
            loadLevel(Levels.NEXT);
            wonFlag = false;
        }

    }

    public void draw(Graphics g) {
        gameLevel.draw(g, screen);

    }

}
