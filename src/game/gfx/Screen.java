package game.gfx;

import game.level.Lighting;

import java.util.Arrays;
import java.util.List;


public class Screen {

    public static final int MAP_WIDTH = 64;
    public static final int MAP_WIDTH_MASK = MAP_WIDTH-1;

    public static final byte BIT_MIRROR_X = 0x1;
    public static final byte BIT_MIRROR_Y = 0x2;

    public static List<Integer> defaultIgnoreColors = Arrays.asList(0xfa05f0);

    public int[] pixels;

    public int xOffset = 0;
    public int yOffset = 0;

    public int  width;
    public int height;

    public SpriteSheet sheet;

    private Lighting lighting;


    public Screen(int width, int height, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.sheet = sheet;

        this.pixels = new int[width*height];
    }

    public void render(int xPos, int yPos, int tile, int mirrorDir, int scale) {
        render(xPos, yPos, tile, mirrorDir, scale, 8, defaultIgnoreColors);
    }

    public void render(int xPos, int yPos, int tile, int mirrorDir, int scale, int block) {
        render(xPos, yPos, tile, mirrorDir, scale, block, defaultIgnoreColors);
    }

    public void render(int xPos, int yPos, int tile, int mirrorDir, int scale, int block, List<Integer> ignoreColors) {
        render(xPos, yPos, this.sheet, tile, mirrorDir, scale, block, ignoreColors);
    }


    public void render(int xPos, int yPos, SpriteSheet sheet, int tile, int mirrorDir, int scale, int block) {
        render(xPos, yPos, sheet, tile, mirrorDir, scale, block, defaultIgnoreColors);
    }

    public void render(int xPos, int yPos, SpriteSheet sheet, int tile, int mirrorDir, int scale, List<Integer> ignoreColors) {
        render(xPos, yPos, sheet, tile, mirrorDir, scale, 8, ignoreColors);
    }


    public void render(int xPos, int yPos, SpriteSheet sheet, int tile, int mirrorDir, int scale, int block, List<Integer> ignoreColors) {
        int logBlock = log2(block);

        if (logBlock == -1) throw new RuntimeException("Invalid block");

        xPos -= xOffset;
        yPos -= yOffset;

        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

        int scaleMap = scale-1;
        int xTile = tile%(sheet.width >> logBlock);
        int yTile = tile/(sheet.width >> logBlock);
        int tileOffset = (xTile << logBlock)+(yTile << logBlock)*sheet.width;


        for (int y = 0;y < block;y++) {
            int ySheet = y;
            if (mirrorY)
                ySheet = block-y-1;

            int yPixel = y+yPos+(y*scaleMap)-((scaleMap << logBlock)/2);

            for (int x = 0;x < block;x++) {
                int xSheet = x;
                if (mirrorX)
                    xSheet = block-x-1;
                int xPixel = x+xPos+(x*scaleMap)-((scaleMap << logBlock)/2);

                int col = sheet.pixels[xSheet+ySheet*sheet.width+tileOffset];


                if (!ignoreColors.contains(col)) {
                    for (int yScale = 0;yScale < scale;yScale++) {
                        if (yPixel+yScale < 0 || yPixel+yScale >= height)
                            continue;
                        for (int xScale = 0;xScale < scale;xScale++) {
                            if (xPixel+xScale < 0 || xPixel+xScale >= width)
                                continue;
                            pixels[(xPixel+xScale)+(yPixel+yScale)*width] =
                                    !lighting.renderLight ? col : colorSelector(col, lighting.lightCombiner((xPixel+xScale)+(yPixel+yScale)*width));
                        }
                    }
                }
            }
        }
    }


    public void setLighting(Lighting lighting) {
        System.out.println("Screen "+lighting);
        this.lighting = lighting;
    }


    private int colorSelector(int color, Integer filter) {
        if (filter == null) return colorSelector(color, lighting.filterColor);
        if (filter > Math.abs(0xff)) {
            int r = (filter/0x10000)%0x100;
            int g = (filter/0x100)%0x100;
            int b = filter%0x100;


            return colorSelector(color, r, g, b);
        }

        return colorSelector(color, filter, filter, filter);
    }

    private static int colorSelector(int color, int rFilter, int gFilter, int bFilter) {

        int r = (color/0x10000)%0x100;
        int g = (color/0x100)%0x100;
        int b = color%0x100;

        List<Integer> rgb = Arrays.asList(r, g, b);
        List<Integer> filters = Arrays.asList(rFilter, gFilter, bFilter);

        for (int i = 0;i < 3;i++) {
            if (rgb.get(i) + filters.get(i) < 0) rgb.set(i, 0);
            else if (rgb.get(i) + filters.get(i) > 0xff) rgb.set(i, 0xff);
            else rgb.set(i, rgb.get(i)+filters.get(i));
        }

        return (rgb.get(0) << 16)+(rgb.get(1) << 8)+rgb.get(2);
    }

    private static int log2(int block) {
        for (int t = 1, x = 0; t <= block; t *= 2, x++) {
            if (t == block) {
                return x;
            }
        }
        return -1;
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;


    }

    
}

