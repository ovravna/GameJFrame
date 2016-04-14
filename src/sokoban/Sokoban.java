package sokoban;

import game.Game;
import game.GameListener;
import game.entities.Player;
import game.gfx.Light;
import game.gfx.Screen;
import game.level.Level;
import sokoban.cells.Box;
import sokoban.cells.Lantern;

public class Sokoban implements GameListener {
    Game game;
    Player player;
    Screen screen;
    Level level;

    public Sokoban() {
        game = new Game("Sokoban",this);
        new Lantern(level, "lantern", 10, 50, Light.SOFT);
        new Box(level, "box", 40, 40);
//        new Ball(level, "Ball", player.x, player.y);
        game.setDaylightCycle(false);
        game.setCycleTime(1);
        game.setLight(-0xff);

        game.start();
    }


    public static void main(String[] args) {
        new Sokoban();
    }

    @Override
    public void newScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void newLevel(Level level) {
        this.level = level;
    }

    @Override
    public void newPlayer(Player player) {
        this.player = player;
    }
}
