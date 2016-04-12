package sokoban;

import game.Game;
import game.GameListener;
import game.entities.Mob;
import game.entities.Player;
import game.gfx.Screen;
import game.level.Level;
import sokoban.cells.Box;

public class Sokoban implements GameListener {
    Game game;
    Player player;
    Screen screen;
    Level level;

    public Sokoban() {
        game = new Game("Sokoban",this);
//        Mob ball = new Ball(level, "Ball", player.x, player.y, player);
        Mob box = new Box(level, "Box", 40, 40);

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
