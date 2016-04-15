package sokoban;

import game.Game;
import game.GameListener;
import game.InputHandler;
import game.entities.Player;
import game.gfx.Screen;
import game.level.Level;
import sokoban.cells.*;

import java.util.ArrayList;
import java.util.List;

public class Sokoban implements GameListener {
    Game game;
    Player player;
    Screen screen;
    Level level;
    InputHandler input;
    private List<List<Boolean>> frame = new ArrayList<>();

    static String q =
                    "#######\n"+
                    "#.  # #\n"+
                    "#$* $ #\n"+
                    "#  $@ #\n"+
                    "# ..  #\n"+
                    "#  *  #\n"+
                    "#######";




    public Sokoban() {
        game = new Game("Sokoban",this);
//        new Lantern(level, "lantern", 10, 50, Light.SOFT);
//        new Wall(level, 50, 50);
//        new Box(level, 10, 50);
//        new Goal(level, 40, 50);
        game.player = new Player(level, 0, 50, input);
        new Box(level, 10, 80);
        new Goal(level, 40, 80);
//
        new Lantern(level, 20, 10);
        new Lantern(level, 60, 10);

        init(q);



//        new Box(level, "box", 40, 40);
//        new Ball(level, "Ball", player.x, player.y);
        game.setLighting(true);
        game.setDaylightCycle(false);
        game.setCycleTime(1);
        game.setLight();


        game.start();
    }


    public void init(String sokoban) {
        String[] sokobanSplit = sokoban.split("\\||\\n");

        int sokoban_width = sokobanSplit[0].length();
        int sokoban_height = sokobanSplit.length;

        int x_level_centrum = (level.width<<3)/2;
        int y_level_centrum = (level.height << 3)/2;

        int xOrigo = (x_level_centrum-(sokoban_width<<3));
        int yOrigo = (y_level_centrum-(sokoban_height<<3));
        
        int x = xOrigo;
        int y = yOrigo;

        for (char cell : sokoban.toCharArray()) {
            switch (cell) {
                case '\n':
                case '|':
                    frame.add(new ArrayList<>());
                    x = xOrigo;
                    y += 16;
                    continue;
                case ' ':
                    break;
                case '#':
                    new Wall(level, x, y);
                    break;
                case '.':
                    new Goal(level,x, y);
                    break;
                case '@':
                    game.player = new Player(level, x, y, input, "Player");
                    break;
                case '$':
                    new Box(level, x, y);
                    break;
                case '*':
                    new Box(level, x, y);
                    new Goal(level, x, y);
                    break;
                case '+':
                    game.player = new Player(level, x, y, input, "Player");
                    new Goal(level, x, y);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("%s not allowed", cell));
            }
            x += 16;
        }
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

    @Override
    public void newInputHandler(InputHandler input) {
        this.input = input;
    }
}
