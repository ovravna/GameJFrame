package game.level;

import game.InputHandler;
import game.InputManager;
import game.gfx.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.level.Levels.MENU;

public class LevelManager extends InputManager {
    private final Screen screen;
    private List<Level> levels = new ArrayList<>();
    private Levels currentLevel;
    private Level gameLevel;



    public LevelManager(Screen screen, InputHandler input) {
        this.screen = screen;

        System.out.println("LevelManager "+input);
        super.setInput(input);
        currentLevel = MENU;
        screen.setLighting(false);

//        Level level = new Level(Sokoban.LEVEL);
//        new Player(level, 0, 0);
////        new Lantern(level, 20, 10, -0x000000);
////            new Lantern(level, 30, 10, -0x11);
//
////            new LightPoint(level, 40, 40, -0x661100, 20);
//
//        loadLevel(level);



        loadLevel(currentLevel);

    }



    public void loadLevel(Level level) {
        gameLevel = level;


//        if (gameLevel.containsPlayer()) {
//            gameLevel.player.setInput(input);
//        }

        gameLevel.loadLevel();
    }

    public void loadLevel(Levels currentLevel) {
        gameLevel = null;
        switch (currentLevel) {
            case MENU:
                gameLevel = new Menu(this, "/levels/Black.png");
                break;
            case FIRST_LEVEL:
                gameLevel = levels.get(0);
        }

//        if (gameLevel.containsPlayer()) {
//            gameLevel.player.setInput(input);
//        }

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
