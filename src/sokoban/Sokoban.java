package sokoban;

import game.Game;
import game.InputHandler;
import game.entities.Player;
import game.level.Level;
import sokoban.cells.Box;
import sokoban.cells.Goal;
import sokoban.cells.LightPoint;
import sokoban.cells.Wall;

public class Sokoban {
    Game game;
    Player player;
    Level level;
    InputHandler input;

    static String q =
                    "#######\n"+
                    "#*  # #\n"+
                    "# *   #\n"+
                    "# .$@ #\n"+
                    "# *   #\n"+
                    "#  *  #\n"+
                    "#######";
    static String w =
            "#######\n"+
                    "#.  # #\n"+
                    "#$* $ #\n"+
                    "#  $@ #\n"+
                    "# ..  #\n"+
                    "#  *  #\n"+
                    "#######";

    static String p = "           ###\n"+
            "          #   #\n"+
            "         #  *  #\n"+
            "        #  *$*  #\n"+
            "        # * * * #\n"+
            "        #  *$*  #\n"+
            "         #  *  #\n"+
            "          #   #\n"+
            "   ###     # #     ###\n"+
            "  #   #   #   #   #   #\n"+
            " #  *  # #  .  # #  *  #\n"+
            "#  * *  #  . .  #  * *  #\n"+
            "# *$*$*   .$+ .   *$*$* #\n"+
            "#  * *  #  . .  #  * *  #\n"+
            " #  *  # #  .  # #  *  #\n"+
            "  #   #   #   #   #   #\n"+
            "   ###     # #     ###\n"+
            "          #   #\n"+
            "         #  *  #\n"+
            "        #  *$*  #\n"+
            "        # * * * #\n"+
            "        #  *$*  #\n"+
            "         #  *  #\n"+
            "          #   #\n"+
            "           ###\n";



    public Sokoban(String board) {
        this.level = init(board);

        game = new Game("Sokoban", this.level);

//        game.player = new Player(level, 0, 50, input);
//        new Lantern(level, 20, 10, 0);
//        new Lantern(level, 60, 10, 0);


//        new Box(level, "box", 40, 40);
//        new Ball(level, "Ball", player.x, player.y);

        game.setLighting(true);
        game.setDaylightCycle(false);
        game.setCycleTime(20);
        game.setLight();
        game.init();

        game.start();
    }


    public static Level init(String sokoban) {
        Level level = new Level("/levels/sokoban_test.png");
        String[] sokobanSplit = sokoban.split("\\||\\n");

        int sokoban_width = sokobanSplit[0].length();
        int sokoban_height = sokobanSplit.length;

        int x_level_centrum = (level.width << 3)/2;
        int y_level_centrum = (level.height << 3)/2;
//        System.out.printf("%s %s", x_level_centrum, y_level_centrum);

        new LightPoint(level, x_level_centrum, y_level_centrum, sokoban_height << 3, 0x33);

        int xOrigo = (x_level_centrum-(sokoban_width << 3));
        int yOrigo = (y_level_centrum-(sokoban_height << 3));


        int x = xOrigo;
        int y = yOrigo;

        for (char cell : sokoban.toCharArray()) {
            switch (cell) {
                case '\n':
                case '|':
                    x = xOrigo;
                    y += 16;
                    continue;
                case ' ':
                    break;
                case '#':
                    new Wall(level, x, y);
                    break;
                case '.':
                    new Goal(level, x, y);
                    break;
                case '@':
                    new Player(level, x, y, "Player");
                    break;
                case '$':
                    new Box(level, x, y);
                    break;
                case '*':
                    new Box(level, x, y);
                    new Goal(level, x, y);
                    break;
                case '+':
                    new Player(level, x, y,  "Player");
                    new Goal(level, x, y);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("%s not allowed", cell));
            }
            x += 16;
        }
        return level;
    }


    public Level getLevel() {
        return level;
    }

    public static void main(String[] args) {
        new Sokoban(w);
    }


//    @Override
//    public void newInputHandler(InputHandler input) {
////        this.input = input;
//    }
//
//    @Override
//    public void action() {
//
//    }
}
