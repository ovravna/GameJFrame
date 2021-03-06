package game.entities;


import game.InputObject;
import game.gfx.Screen;
import game.level.Level;
import sokoban.cells.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Entity implements Comparable{

    public int x, y;
    protected Level level;
    protected boolean solid;
    private List<Class> renderOrder = Arrays.asList(Player.class, Lantern.class, Ball.class, Box.class, Goal.class, Wall.class);


    /**
     * xMax, xMin, yMax, yMin
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





//        if (!(this instanceof Mob)) return false;

    protected boolean isSolidEntity(int xa, int ya, int x, int y) {
        if (level == null) return false;

//        if (!(this instanceof Player)) {
//            return true;
//        }

        x += this.x+xa;
        y += this.y+ya;

        Entity e;
        for (Entity entity : level.entities) {
            if (!entity.equals(this) && (e = entity.getEntity(x, y)) != null) {
                if (e.isSolid() && !e.equals(this)) {
                    if (e instanceof Mob && this instanceof InputObject) {
                        e.isPushed(xa, ya);
                    }
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

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
            if (entities.get(0).equals(this)) {
                for (Entity entity : entities) {
                    if (!entity.equals(this)) {
                        return entity;
                    }
                }
            }
            return entities.get(0);
        }
        return null;
    }

    public void removeEntity(Entity entity) {
        level.entities.remove(entity);
    }

    public void removeEntity(int x, int y) {
        Entity entity = getEntity(x, y);
        level.entities.remove(entity);

//        level.entities.remove(getEntity(x, y));
    }

    public void changePosition() {
        changePosition(0, -300);
    }

    public void changePosition(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public abstract void isPushed(int x, int y);

    @Override
    public int compareTo(Object o) {
        if (o instanceof Entity)
            return renderOrder.indexOf(o.getClass())-renderOrder.indexOf(this.getClass());
        else return 0;
    }

}
