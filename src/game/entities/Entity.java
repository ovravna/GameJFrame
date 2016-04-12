package game.entities;


import game.gfx.Screen;
import game.level.Level;
import sokoban.cells.Ball;
import sokoban.cells.Box;
import sokoban.cells.Lantern;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {

    public int x, y;
    protected Level level;
    protected boolean solid;

    /**
     * xMax, yMax, xMin, yMin
     */
    protected int[] dimentions = new int[4];


    public Entity(Level level) {
        init(level);
        level.addEntities(this);
    }

    private final void init(Level level) {
        this.level = level;
    }

    public abstract void tick();

    public abstract void render(Screen screen);

    public boolean isColiding(int x, int y) {
        if (!solid) return false;

        int dx = x-this.x;
        int dy = y-this.y;

        return dx < 16 && dx >= 0 && dy < 16 && dy >= 0;
    }

    public boolean isSolid() {
        return solid;
    }

    protected boolean isSolidEntity(int x, int y) {
        return isSolidEntity(0, 0, x, y);
    }

    //        Mob mob = (Mob) this;
//        if (!(this instanceof Mob)) return false;





    protected boolean isSolidEntity(int xa, int ya, int x, int y) {
        if (level == null) return false;

        x += this.x+xa;
        y += this.y+ya;

        Entity e;
        for (Entity entity : level.entities) {
            if (!entity.equals(this) && (e = entity.getEntity(x, y)) != null) {
                if (e.isSolid()) {
                    if (e instanceof Mob) {
                        e.isPushed(xa, ya);
                    }
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

//    private boolean isPushed(int xa, int ya) {
//        return mob.isPushed(xa, ya);
//
//        return false;
//    }



//    protected boolean thigs() {
//        Tile lastTile = level.getTile((this.x+x) >> 3, (this.y+y) >> 3);
//        Tile newTile = level.getTile((this.x+x+xa) >> 3, (this.y+y+ya) >> 3);
//        if (!lastTile.equals(newTile) && newTile.isSolid())
//            return true;
//    }


    public boolean entityOn(int x, int y) {
        int dx = x-this.x;
        int dy = y-this.y;

        if (dx < dimentions[0] && dx >= dimentions[1] && dy < dimentions[2] && dy >= dimentions[3]) {
            return true;
        }
        return false;

    }

    public Entity getEntity(int x, int y) {
        List<Entity> entities = new ArrayList<>();



        for (int i = 0;i < level.entities.size();i++) {
            Entity e = level.entities.get(i);
            if (e.entityOn(x, y)) {
                entities.add(e);
            }
        }

//                level.entities.stream().
//                filter(n -> n.entityOn(x, y)).collect(Collectors.toList());

        if (entities.size() > 0) {
            return entities.get(0);
        }
        return null;
    }

    public abstract void isPushed(int x, int y);

    public int compareTo(Entity entity) {

        if (this instanceof Lantern) return 1;

        if (this instanceof Player) return 1;

        if (this instanceof Ball) return 1;

        if (this instanceof Box) return -1;


        return -1;
    }

}
