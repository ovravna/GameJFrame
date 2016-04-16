package game.level;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    List<Level> levels = new ArrayList<>();
    Levels currentLevel;



    public LevelManager() {
        currentLevel = Levels.MENU;

        loadLevel(currentLevel);




    }

    private void loadLevel(Levels currentLevel) {


    }

    public void addLevel(Level level) {
        if (level == null || levels.contains(level)) {
            return;
        }
        levels.add(level);
    }


}
