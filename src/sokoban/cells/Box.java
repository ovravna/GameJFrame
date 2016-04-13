package sokoban.cells;

import game.entities.Mob;
import game.gfx.Colors;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

public class Box extends Mob implements Actable {

    private int color = Colors.get(432, 543, 432, 000);
    private SpriteSheet sheet = new SpriteSheet("/wall16x16.png");
    private boolean isHeld;

    public Box(Level level, String name, int x, int y) {
        super(level, name, x, y, 1);
        solid = true;
        pushable = true;
        dimentions = new int[]{15, 0, 16, 0};

    }


    @Override
    public void tick() {
        if (level.player == null) return;

        if (isHeld) {
            int xa = level.player.getXa();
            int ya = level.player.getYa();
            move(xa, ya);
            double distSqrt = Math.pow(level.player.x-x, 2)
                    +Math.pow(level.player.y-y, 2);

            if (distSqrt > 900) {
                isHeld = false;
            }
        }




//        this.isHeld = false;

//        if (level.player )

    }

    @Override
    public void render(Screen screen) {
        screen.render(x, y, sheet,0, 0, 1, 16);

    }

    @Override
    public void isPushed(int x, int y) {
        move(x, y);

    }

    @Override
    public void act() {
        this.isHeld = !isHeld;

    }
}
