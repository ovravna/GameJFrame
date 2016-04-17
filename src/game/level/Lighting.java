package game.level;

import game.entities.Entity;
import game.gfx.Light;
import game.gfx.Screen;

import java.util.HashMap;


public class Lighting {

    private HashMap<Entity, Integer[]> lightSources = new HashMap<>();
    public static final int INITIAL_FILTER = -0xdf;
    public static int filterColor = INITIAL_FILTER;

    public boolean renderLight = true;

    private Screen screen;
    private int width;
    private int height;

    public Lighting(Screen screen) {

        this.screen = screen;
        width = screen.width;
        height = screen.height;
        screen.setLighting(this);

        setFilterColor();

    }

    public void renderRoundLight(int x, int y, int radius, int filter, Entity this_entity) {
        renderRoundLight(x, y, radius, filter, Light.SOFT, this_entity);
    }

    public void renderRoundLight(int x, int y, int radius, int filter, Light light, Entity this_entity) {
        renderRoundLight(x, y, radius, filter, 2, 0, light, this_entity);
    }

    public void renderRoundLight(int x, int y, int radius, int filter, int xOff, int yOff, Entity this_entity) {
        renderRoundLight(x, y, radius, filter, xOff, yOff, Light.SOFT, this_entity);
    }

    public void renderRoundLight(int x, int y, int radius, int filter, int xOffset, int yOffset, Light lighting, Entity this_entity) {
        Integer[] light = new Integer[width*height];
        boolean rgbFilter = false;

        int radSqur = radius*radius;

        x -= screen.xOffset-xOffset;
        y -= screen.yOffset-yOffset;

        double distance;

        int a, r = 0, g = 0, b = 0;
        if (Math.abs(filter) > 0xff) {
            a = filter >> 24;
            r = filter >> 16;
            g = filter >> 8;
            b = filter;
            rgbFilter = true;

        } else a = filter;


        for (int xa = 0;xa < width;xa++) {
            for (int ya = 0;ya < height;ya++) {
                distance = Math.pow(xa-x, 2)+Math.pow(ya-y, 2);

//                if (distance < radSqur*0.01) {
//                    this.light[xa+ya*width] = filter;
//                } else

                // TODO: 14.04.2016 ender kode for håndtering av filter > 0xff
                if (distance < radSqur) {

                    int shade = 1;

                    if (lighting == Light.SOFT) {
                        shade = ((int) ((filterColor*distance)-a*(radSqur-distance))/radSqur);
                    } else if (lighting == Light.HARD) {
                        shade = a;
                    }

//                    System.out.println(temp.length);
                    if (rgbFilter) {
                        int f = (shade-a)/filterColor;


                        light[xa+ya*width] = ((f*r) << 16)+((f*b) << 8)+(f*g);

                    } else light[xa+ya*width] = shade;

                } else
//                if (distance < radSqur*1.1)
                    light[xa+ya*width] = filterColor;
            }
        }
        lightSources.put(this_entity, light);
    }


    public void setFilterColor() {
        setFilterColor(INITIAL_FILTER);
    }

    public void setFilterColor(int filterColor) {
        this.filterColor = filterColor;
    }


    public Integer lightCombiner(int i) {
        int r = -0xff;

        Integer temp;

        for (Integer[] light : lightSources.values()) {
            temp = light[i];

            if (temp == null) {
                temp = filterColor;
            }
            r = temp > r ? temp:r;
        }

        if (r == -0xff) {
            r = filterColor;
        }

        return r;
    }

    public void removeLightSource(Entity entity) {
        lightSources.remove(entity);
    }

    private int clock;

    public boolean setFilter(int cycleSeconds, boolean rise, int maxFilter) {
        double time = 60*cycleSeconds;


//        filterColor = (int) (Game.getLight()*(1-Math.sin(2*(clock/time)))-maxFilter);
        int delta = (INITIAL_FILTER-maxFilter)/2;

        filterColor = (int) (delta*Math.cos((clock << 1)/time)+delta+maxFilter);

        clock++;
        System.out.println(filterColor);
        if (rise) {
            return filterColor > 0.9*maxFilter;
        } else
            return filterColor < INITIAL_FILTER;

    }

}
