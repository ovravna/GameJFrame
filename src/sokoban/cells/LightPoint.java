package sokoban.cells;

import game.entities.Mob;
import game.gfx.Screen;
import game.level.Level;

public class LightPoint extends Mob {
    private int radius;
    private int filter;

    public LightPoint(Level level, int x, int y, int radius, int filter) {
        super(level, "Light", x, y, 0);

        this.radius = radius;
        this.filter = filter;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Screen screen) {
        screen.renderRoundLight(x, y, radius, filter, this);

    }

    @Override
    public void isPushed(int x, int y) {

    }
}
