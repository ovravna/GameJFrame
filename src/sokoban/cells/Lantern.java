package sokoban.cells;

import game.entities.Mob;
import game.gfx.Screen;
import game.level.Level;

import java.util.Arrays;

public class Lantern extends Mob implements Actable{


    public Lantern(Level level, String name, int x, int y) {
        super(level, name, x, y, 1);
        solid = true;
        pushable = false;
        dimentions = new int[]{14, 2, 14, 2};

    }

    @Override
    protected boolean hasColided(int xa, int ya) {
        return false;
    }

    @Override
    public void tick() {

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
        System.out.println("kake");

    }
}