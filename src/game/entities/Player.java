package game.entities;

//import java.util.*;

import game.InputHandler;
import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Player extends Mob {

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);
    private int scale = 1;
    protected boolean isSwimming = false;
    private int tickCount = 0;
    private String username;

    public Player(Level level, int x, int y, InputHandler input, String username) {
        super(level, "Player", x, y, 1);
        this.input = input;
        this.username = username;
        dimentions = new int[]{7, 0, 7, 3};
    }

    @Override
    public void tick() {

        int xa = 0;
        int ya = 0;

        if (input.up.isPressed()) ya--;
        if (input.down.isPressed()) ya++;
        if (input.left.isPressed()) xa--;
        if (input.right.isPressed()) xa++;

        if (xa != 0 || ya != 0) {
            move(xa, ya);
            isMoving = true;
        } else {
            isMoving = false;
        }

        if (level.getTile(this.x >> 3, this.y >> 3).getId() == 3) {
            isSwimming = true;
        }
        if (isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != 3) {
            isSwimming = false;
        }
        tickCount++;
    }

    @Override
    public void render(Screen screen) {
        int xTile = 0;
        int yTile = screen.sheet.playerLine;

        int walkingSpeed = 3;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;

        if (moveingDir == 1) {
            xTile += 2;
        } else if (moveingDir > 1) {
            xTile += 4+((numSteps >> walkingSpeed) & 1)*2;
            flipTop = (moveingDir-1)%2;
        }

        int modifier = 8*scale;
        int xOffset = x-modifier/2;
        int yOffset = y-modifier/2-4;

        if (isSwimming) {
            List<Integer> waterColor = new ArrayList<>(Arrays.asList(0x000000, 0x4444ff, 0x0000ff, 0x8888ff));

            yOffset += 4;
            if (tickCount%60 < 15) {
                waterColor.remove((Integer)0x4444ff);
            } else if (15 <= tickCount%60 && tickCount%60 < 30) {
                yOffset--;
                waterColor.remove((Integer)0x3333ff);
            } else if (30 <= tickCount%60 && tickCount%60 < 45) {
                waterColor.remove((Integer)0x8888ff);
            } else {
                yOffset--;
                waterColor.remove((Integer)0x4444ff);
            }

            screen.render(xOffset, yOffset+3, 6, 0, 1, 8, waterColor);
            screen.render(xOffset+8, yOffset+3, 6, 1, 1, 8 , waterColor);

        }

//        if (getEntity(20, 20) != null) {
//            System.out.println("Kake!");
//        }

        screen.render(xOffset + (modifier * flipTop), yOffset,
                xTile+yTile*(screen.sheet.width >> 3), flipTop, scale);

        screen.render(xOffset+modifier- (modifier * flipTop), yOffset,
                (xTile+1)+yTile*(screen.sheet.width >> 3), flipTop, scale);

        if (!isSwimming) {
            screen.render(xOffset + (modifier * flipBottom), yOffset+modifier,
                    xTile+(yTile+1)*(screen.sheet.width >> 3), flipBottom, scale);

            screen.render(xOffset+modifier - (modifier * flipBottom), yOffset+modifier,
                    (xTile+1)+(yTile+1)*(screen.sheet.width >> 3), flipBottom, scale);
        }

//        if (username != null) {
//            Font.render(username, screen, xOffset- ((username.length()-1) / 2 * 8),
//                    yOffset-10, Colors.get(-1, -1, -1, 555), 1);
//        }

    }

    @Override
    public void isPushed(int x, int y) {
    }


    @Override
    public boolean hasColided(int xa, int ya) {
        int xMax = dimentions[0];
        int xMin = dimentions[1];
        int yMax = dimentions[2];
        int yMin = dimentions[3];

//        if (isSolidEntity(xa, ya, x, y)) {
//            return true;
//        }
        for (int x = xMin;x < xMax;x++) {
            if (isSolidTile(xa, ya, x, yMin)) {
                return true;
            } else if (isSolidEntity(xa, ya, x, yMin)) {
                return true;
            }

        }
        for (int x = xMin;x < xMax;x++) {
            if (isSolidTile(xa, ya, x, yMax)) {
                return true;
            } else if (isSolidEntity(xa, ya, x, yMax)) {
                return true;
            }
        }

        for (int y = yMin;y < yMax;y++) {
            if (isSolidTile(xa, ya, xMin, y)) {
                return true;
            } else if (isSolidEntity(xa, ya, xMin, y)) {
                return true;
            }
        }

        for (int y = yMin;y < yMax;y++) {
            if (isSolidTile(xa, ya, xMax, y)) {
                return true;
            } else if (isSolidEntity(xa, ya, xMax, y)) {
                return true;
            }
        }

        return false;
    }


}
