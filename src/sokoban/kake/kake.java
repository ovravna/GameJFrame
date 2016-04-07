package sokoban.kake;

public class kake {



}


//        public void render(int xPos, int yPos, SpriteSheet sheet, int tile, int color, int mirrorDir, int scale, int block) {
//
////        if (log(block) == -1) throw new RuntimeException("Invalid block");
//
//        xPos -= xOffset;
//        yPos -= yOffset;
//
//
//        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
//        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
//
//        int scaleMap = scale-1;
//        int xTile = tile%(sheet.width >> 3);
//        int yTile = tile/(sheet.width >> 3);
//        int tileOffset = (xTile << 3)+(yTile << 3)*sheet.width;
//        for (int y = 0;y < 8;y++) {
//            int ySheet = y;
//            if (mirrorY)
//                ySheet = 7-y;
//
//            int yPixel = y+yPos+(y*scaleMap)-((scaleMap << 3)/2);
//
//            for (int x = 0;x < 8;x++) {
//                int xSheet = x;
//                if (mirrorX)
//                    xSheet = 7-x;
//                int xPixel = x+xPos+(x*scaleMap)-((scaleMap << 3)/2);
//                int col = (color >> sheet.pixels[xSheet+ySheet*sheet.width+tileOffset]*8) & 255;
//                if (col < 255) {
//                    for (int yScale = 0;yScale < scale;yScale++) {
//                        if (yPixel+yScale < 0 || yPixel+yScale >= height)
//                            continue;
//                        for (int xScale = 0;xScale < scale;xScale++) {
//                            if (xPixel+xScale < 0 || xPixel+xScale >= width)
//                                continue;
//                            pixels[(xPixel+xScale)+(yPixel+yScale)*width] = col;
//                        }
//                    }
//                }
//            }
//        }
//    }
