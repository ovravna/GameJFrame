package sokoban.cells;

import game.entities.Mob;
import game.gfx.Screen;
import game.level.Level;

import java.util.Arrays;

public class Lantern extends Mob implements Actable{
    private boolean isHeld;


    public Lantern(Level level, String name, int x, int y) {
        super(level, name, x, y, 1);
//        solid = true;
//        pushable = false;
        dimentions = new int[]{14, 2, 14, 2};

    }

    public boolean isHeld() {
        return isHeld;
    }

    @Override
    protected boolean hasColided(int xa, int ya) {
        return false;
    }

    @Override
    public void tick() {
        if (isHeld()) {
            x = level.player.x;
            y = level.player.y;
        }

    }

    @Override
    public void render(Screen screen) {

        screen.setRoundLight(x, y, 60, 0x01, 6, 6);
        screen.render(x, y, 4+5*8, 0, 1, 16, Arrays.asList(0xffffff));

    }

    @Override
    public void isPushed(int x, int y) {

    }

    @Override
    public void act() {
        isHeld = !isHeld;
        System.out.println("kake");

    }
}
