package sokoban.cells;

import game.entities.Mob;
import game.entities.Player;
import game.gfx.Colors;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ball extends Mob {
    private SpriteSheet sheet = new SpriteSheet("/box16x16.png");
    private int color = Colors.get(555, 334, 445 , -1);
    Player player;
    List<Integer> xPos = new ArrayList<>();
    List<Integer> yPos = new ArrayList<>();



    public Ball(Level level, String name, int x, int y, Player player) {
        super(level, name, x, y, player.speed);
        this.player = player;
        solid = true;

    }



//    @Override
//    protected boolean hasColided(int xa, int ya) {
//        return xa == player.x || ya == player.y;
//    }

    int lastMove = 0;
    int lag = 50;

    @Override
    public void tick() {
        this.speed = player.speed;

        if (player.isMoving) {
            xPos.add(player.x);
            yPos.add(player.y);
            lastMove = 0;
            lag = 50;
        } else {
            if (lastMove < 30) {
                lag--;
            }

            lastMove++;
        }

        if (xPos.size() > lag && yPos.size() > lag) {

            int xTemp = xPos.get(xPos.size()-lag);
            int yTemp = yPos.get(yPos.size()-lag);
            x = xTemp;
            y = yTemp;

//            if (!hasColided(xTemp, yTemp)) {
//            }

            xPos = xPos.subList(xPos.size()-lag, xPos.size());
            yPos = yPos.subList(yPos.size()-lag, yPos.size());
        }


    }

    @Override
    public void render(Screen screen) {

//        screen.render(x,y,10 + 10 * 16,color,0,1);
        screen.render(x,y,4+5*8, 0, 1, 16, Arrays.asList(0xffffff));




    }

    @Override
    public void isPushed(int x, int y) {
        return;
    }


    @Override
    protected boolean hasColided(int xa, int ya) {
        return false;
    }
}
