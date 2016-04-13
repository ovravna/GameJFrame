package sokoban.cells;

import game.entities.Mob;
import game.gfx.Light;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

import java.util.Arrays;

public class Lantern extends Mob implements Actable{
    private boolean isHeld;
    Light light;

    public Lantern(Level level, String name, int x, int y, Light light) {
        super(level, name, x, y, 1);
//        solid = true;
//        pushable = true;
        this.light = light;
        dimentions = new int[]{14, 2, 14, 2};

    }

    public boolean isHeld() {
        return isHeld;
    }

    @Override
    public void tick() {
        if (isHeld) {
            x = level.player.x-3;
            y = level.player.y-6;
        }

    }

    @Override
    public void render(Screen screen) {

        screen.setRoundLight(x, y, 100, 0, 6, 6, light);

        if (!isHeld) {
            screen.render(x, y, new SpriteSheet("/box16x16.png"), 0 , 0, 1, 16, Arrays.asList(0xffffff));
        }

    }

    @Override
    public void isPushed(int x, int y) {
        move(x, y);

    }

    @Override
    public void act() {
        isHeld = !isHeld;

    }


}
