package game.level;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    List<Level> levels = new ArrayList<>();


    public void addLevel(Level level) {
        if (level == null || levels.contains(level)) {
            return;
        }
        levels.add(level);
    }





}
