package game;

import game.entities.Player;
import game.gfx.Screen;
import game.level.Level;

public interface GameListener {

    void newScreen(Screen screen);

    void newLevel(Level level);

    void newPlayer(Player player);

    void newInputHandler(InputHandler input);

    void action();
}
