package game.gfx;

import java.util.Arrays;
import java.util.List;

public class Screen {

    public static final int MAP_WIDTH = 64;
    public static final int MAP_WIDTH_MASK = MAP_WIDTH-1;

    public static final byte BIT_MIRROR_X = 0x1;
    public static final byte BIT_MIRROR_Y = 0x2;
    private static int filterColor = 0x01; //-0xdf;

    private List<Integer> defaultIgnoreColors = Arrays.asList(0x000000);

    public int[] pixels;
    public int[] light;


    public int xOffset = 0;
    public int yOffset = 0;

    public int  width;
    public int height;

    public SpriteSheet sheet;


    public Screen(int width, int height, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.sheet = sheet;

        this.pixels = new int[width*height];
        this.light = new int[width*height];

//        0x440000;
    }

    public void render(int xPos, int yPos, int tile, int mirrorDir, int scale) {
        render(xPos, yPos, tile, mirrorDir, scale, 8, defaultIgnoreColors);
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
//                    col = Screen.colorSelector(col, -0xcc);
                    for (int yScale = 0;yScale < scale;yScale++) {
                        if (yPixel+yScale < 0 || yPixel+yScale >= height)
                            continue;
                        for (int xScale = 0;xScale < scale;xScale++) {
                            if (xPixel+xScale < 0 || xPixel+xScale >= width)
                                continue;
                            pixels[(xPixel+xScale)+(yPixel+yScale)*width]
                                    = colorSelector(col, light[(xPixel+xScale)+(yPixel+yScale)*width]);
                        }
                    }
                }
            }
        }
    }

//    public static void main(String[] args) {
//        int kake = new Screen(2, 2, new SpriteSheet("/player16x16.png")).colorSelector(0x33dd55, 1);
////        System.out.println(Integer.toHexString(kake));
//
//    }

    public void setRoundLight(int x, int y, int radius, int filter) {
        setRoundLight(x, y, radius, filter, 2, 0);
    }
    public void setRoundLight(int x, int y, int radius, int filter, int xOff, int yOff) {

        int radSqur = radius*radius;

        x -= xOffset - xOff;
        y -= yOffset - yOff;

        double distance;

        for (int xa = 0;xa < width;xa++) {
            for (int ya = 0;ya < height;ya++) {
                distance = Math.pow(xa-x, 2)+Math.pow(ya-y, 2);
                if (distance < radSqur*0.01) {
                    light[xa+ya*width] = filter;
                } else

                if (distance < radSqur) {
                    light[xa+ya*width] = (int)((filterColor-filter)*(distance/radSqur*0.9));

//                            ((filterColor-filter)/(-0.4*radSqur)*(distance - radSqur));
                } else light[xa+ya*width] = 0;
            }
        }
    }

    private static int colorSelector(int color, int filter) {
        if (filter == 0) return Screen.colorSelector(color, filterColor);
        return colorSelector(color, filter, filter, filter);
    }

    private static int colorSelector(int color, int red, int green, int blue) {
        int r = (color/0x10000)%0x100;
        int g = (color/0x100)%0x100;
        int b = color%0x100;

        List<Integer> rgb = Arrays.asList(r, g, b);
        List<Integer> filters = Arrays.asList(red, green, blue);

        for (int i = 0;i < 3;i++) {
            if (rgb.get(i) + filters.get(i) < 0) rgb.set(i, 0);
            else if (rgb.get(i) + filters.get(i) > 0xff) rgb.set(i, 0xff);
            else rgb.set(i, rgb.get(i)+filters.get(i));
        }



//        System.out.printf("%s %s %s\n",Integer.toHexString(r), Integer.toHexString(g), Integer.toHexString(b));
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
















//    public int[] colors = new int[MAP_WIDTH*MAP_WIDTH*4];



//    public void render(int[] pixels, int offset, int row) {
//        for (int yTile = yOffset >> 3;yTile <= (yOffset+height) >> 3;yTile++) {
//            int yMin = yTile*8-yOffset;
//            int yMax = yMin+8;
//
//            if (yMin < 0) yMin = 0;
//            if (yMax > height) yMax = height;
//
//
//            for (int xTile = xOffset >> 3;xTile <= (xOffset+width) >> 3;xTile++) {
//                int xMin = xTile*8-xOffset;
//                int xMax = xMin+8;
//
//                if (xMin < 0) xMin = 0;
//                if (xMax > width) xMax = width;
//
//                int tileIndex = (xTile & MAP_WIDTH_MASK)+(yTile & MAP_WIDTH_MASK)*MAP_WIDTH;
//
//                for (int y = yMin;y < yMax;y++) {
//                    int sheetPixel = ((y+yOffset) & 7)*sheet.width+((xMin+xOffset) & 7);
//                    int tilePixel = offset+xMin+y*row;
//                    for (int x = xMin;x < xMax;x++) {
//                        int color = tileIndex*4+sheet.pixels[sheetPixel++];
//                        pixels[tilePixel++] = colors[color];
//                    }
//                }
//            }
//        }
//    }





















