package game.level;

import game.Game;
import game.InputHandler;
import game.InputManager;
import game.audio.AudioPlayer;
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
        this(screen, input, null);
    }

    public LevelManager(Screen screen, InputHandler input, Level level) {
        this.screen = screen;
        addLevel(level);
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

    public void loadLevel() {
        loadLevel(currentLevel);
    }

    public void loadLevel(Levels currentLevel) {
        gameLevel = null;
        switch (currentLevel) {
            case MENU:
                gameLevel = new Menu(this, "/levels/Black.png");
                break;
            case FIRST_LEVEL:

                if (!levels.isEmpty()) {
                    gameLevel = levels.get(0);
                    System.out.println("GameLevel-level: "+gameLevel);
                    gameLevel.addManager(this);
                } else System.out.println("No first level");

//                gameLevel.renderLight(false);
                break;
            case TEST:
                Level level = new Level(this, Sokoban.LEVEL);
                new Player(level, 0, 0);
//                new Lantern(level, 20, 10, -0xaa);
                new Lantern(level, 30, 10);

//                new LightPoint(level, 40, 40, -0x661100, 20);
                loadLevel(level);
                break;
            case NEXT:
                AudioPlayer.BG_MUSIC.play();
                loadLevel(Sokoban.init(Sokoban.nextBoard()));
                break;
            default:
                throw new RuntimeException("Invalid level");
        }
        System.out.println("Level loading");
        gameLevel.loadLevel();
        System.out.println("Level loaded");
    }

    public void addLevel(Level level) {
        if (level != null && !levels.contains(level)) {
            levels.add(level);
            level.addManager(this);
        }
        System.out.println("Level: "+level);

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

//========= Sokoban spesific win conditions

        if (!wonFlag && gameLevel.entities.stream().anyMatch(n -> n instanceof Goal && ((Goal) n).isWon())) {
            smoothRise = 1;
            cycleTime = 1;
            wonFlag = true;
        }

        if (smoothRise != 0) {
            if (gameLevel.lighting.setFilter(cycleTime, smoothRise == 1, 0xff))
                smoothRise = 0;
        } else if (wonFlag) {
            loadLevel(Levels.NEXT);
            wonFlag = false;
        }
//=========================================

        if (input.restart.isToggled()) {
            loadLevel(currentLevel);
        }

        if (input.meta_data.isToggled()) {
            Game.META_DATA = !Game.META_DATA;
        }


    }

    public void draw(Graphics g) {
        gameLevel.draw(g, screen);

    }

}
