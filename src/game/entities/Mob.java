package game.entities;

import game.level.Level;
import game.level.tiles.Tile;

public abstract class Mob extends Entity {

    protected String name;
    public int speed;
    protected int numSteps = 0;
    public boolean isMoving;
    protected int moveingDir = 1;
    protected int scale = 1;
    protected boolean pushable;

    public Mob(Level level, String name, int x, int y, int speed) {
        super(level);
        pushable = false;
        this.name = name;
        this.speed = speed;
        this.x = x;
        this.y = y;
    }

    public void move(int xa, int ya) {
        if (xa != 0 && ya != 0) {
            move(xa, 0);
            move(0, ya);
            numSteps--;
            return;
        }

        numSteps++;
        if (!hasColided(xa, ya)) {
            if (ya < 0) moveingDir = 0;
            if (ya > 0) moveingDir = 1;
            if (xa < 0) moveingDir = 2;
            if (xa > 0) moveingDir = 3;

            x += xa*speed;
            y += ya*speed;
        } else {
            System.out.println("kake12");
        }
    }

    protected boolean isSolidTile(int xa, int ya, int x, int y) {
        if (level == null)
            return false;
        Tile lastTile = level.getTile((this.x+x) >> 3, (this.y+y) >> 3);
        Tile newTile = level.getTile((this.x+x+xa) >> 3, (this.y+y+ya) >> 3);
        if (!lastTile.equals(newTile) && newTile.isSolid())
            return true;


        return false;
    }



    protected abstract boolean hasColided(int xa, int ya);



}















