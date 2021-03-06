package sokoban.cells;

import game.entities.Mob;
import game.gfx.Light;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

public class Lantern extends Mob implements Actable{
    private boolean isHeld;
    private Light light;
    private final SpriteSheet lanternSheet;
    private int filter;
    private int radius;
    private LightPoint lightPoint;

    private boolean lastHeld;
    private static final int INITIAL_BOUNCETIME = 19;
    private int bouceTime = INITIAL_BOUNCETIME;
    private boolean bounce;

    public Lantern(Level level, int x, int y) {
        this(level, x, y, -0x33);
    }

    public Lantern(Level level, int x, int y, int filter) {
        this(level, x, y, Light.SOFT, filter, 50);
    }

    public Lantern(Level level, int x, int y, Light light, int filter, int radius) {
        super(level, "Lantern", x, y, 1);
//        solid = true;
//        pushable = true;
        this.light = light;
        this.radius = radius;
        dimentions = new int[]{14, 2, 14, 2};
        lanternSheet = new SpriteSheet("/box16x16.png");
        this.filter = filter;
    }

    public boolean isHeld() {
        return isHeld;
    }

    @Override
    public void tick() {
        if (!isHeld && lastHeld) {
            bounce = true;
        }

        if (bounce) {
            if (bouceTime == 0) {
                bounce = false;
                bouceTime = INITIAL_BOUNCETIME;
            }

            if (bouceTime % 10 > 5) {
                y--;
            } else {
                y++;
            }

            bouceTime--;
        }

        if (isHeld) {
            x = level.getPlayer().x-3;
            y = level.getPlayer().y-6;
        }
        lastHeld = isHeld;
    }

    @Override
    public void render(Screen screen) {


        level.lighting.renderRoundLight(x, y, radius, filter, 6, 6, light, this);

        if (!isHeld) {
            screen.render(x, y, lanternSheet, 1 , 0, 1, 16);
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
