package game.entities;

//import java.util.*;

import game.InputHandler;
import game.InputObject;
import game.gfx.Screen;
import game.level.Level;
import sokoban.cells.Actable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Player extends Mob implements Actable, InputObject {

    private InputHandler input;
    private int scale = 1;
    protected boolean isSwimming = false;
    private int tickCount = 0;
    private String username;
    private int xa, ya;
    private boolean isJumping;
    private int jumpTime;

    public Player(Level level, int x, int y) {
        this(level, x, y, "Player");
    }

    public Player(Level level, int x, int y, String username) {
        super(level, "Player", x, y, 1);
        this.username = username;
        dimentions = new int[]{7, 0, 7, 3};
        super.addInputObject(this);
    }

    @Override
    public void setInputHandler(InputHandler input) {
        this.input = input;
        System.out.println("Input from: "+input);
    }

    @Override
    public void tick() {

        if (input == null) {
            return;
        }

        xa = 0;
        ya = 0;

        if (input.up.isPressed()) ya--;
        if (input.down.isPressed()) ya++;
        if (input.left.isPressed()) xa--;
        if (input.right.isPressed()) xa++;
        if (input.act.isReleased()) act();
        if (input.jump.isReleased()) {
            isJumping = true;
            jumpTime = tickCount;
        }


        if (xa != 0 || ya != 0) {
            move(xa, ya);
            isMoving = true;
        } else isMoving = false;

        if (level.getTile(this.x >> 3, this.y >> 3).getId() == 3 && !isJumping) {
            isSwimming = true;
        }
        if (isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != 3) {
            isSwimming = false;
        }
        tickCount++;
    }

    @Override
    public void act() {
        int x;
        int y;
        if (moveingDir/2 == 0) {
            y = moveingDir%2 == 0 ? -1:1;
            x = 0;
        } else {
            y = 0;
            x = moveingDir%2 == 0 ? -1:1;
        }
        int xp;
        int yp;

        for (int i = 1; i < 10; i++) {
            xp = x * i;
            yp = y * i;
            Entity e = getEntity(this.x+3 + xp, this.y + yp);
//            System.out.print(e == null ? "":e.getClass().getSimpleName()+"\n");

            if (e != null && e instanceof Actable && !e.equals(this)) {
                ((Actable) e).act();
                return;
            }
        }
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

//        screen.renderRoundLight(x + 3, y, 10, 0, 1, 0, Light.HARD);

        if (isJumping && !isSwimming) {
            yOffset -= 6;
            scale = 2;
            if (tickCount-jumpTime > 20) {
                isJumping = false;
                scale = 1;
            }
        }
        if (isSwimming) {
            List<Integer> waterColor = new ArrayList<>(Arrays.asList(0xfa05f0, 0x4444ff, 0x0000ff, 0x8888ff));

            yOffset += 4;
            if ((tickCount%60)/15 == 0) {
                waterColor.remove(1);
            } else if ((tickCount%60)/15 == 1) {
                yOffset--;
                waterColor.remove(2);
            } else if ((tickCount%60)/15 == 2) {
                waterColor.remove(3);
            } else {
                yOffset--;
                waterColor.remove(1);
                waterColor.remove(2);
            }

            screen.render(xOffset, yOffset+3, 6, 0, 1, 8, waterColor);
            screen.render(xOffset+8, yOffset+3, 6, 1, 1, 8 , waterColor);

        }






        screen.render(xOffset + (modifier * flipTop), yOffset, xTile+yTile*(screen.sheet.width >> 3), flipTop, scale);

        screen.render(xOffset+modifier- (modifier * flipTop), yOffset, (xTile+1)+yTile*(screen.sheet.width >> 3), flipTop, scale);

        if (!isSwimming) {
            screen.render(xOffset + (modifier * flipBottom), yOffset+modifier, xTile+(yTile+1)*(screen.sheet.width >> 3), flipBottom, scale);

            screen.render(xOffset+modifier - (modifier * flipBottom), yOffset+modifier, (xTile+1)+(yTile+1)*(screen.sheet.width >> 3), flipBottom, scale);
        }

//        if (username != null) {
//            Font.render(username, screen, xOffset- ((username.length()-1) / 2 * 8),
//                    yOffset-10, Colors.get(-1, -1, -1, 555), 1);
//        }

    }

    @Override
    public void isPushed(int x, int y) {
    }


//    @Override
//    public boolean hasColided(int xa, int ya) {
//        int[] xPos = new int[]{dimentions[0], dimentions[1]};
//        int[] yPos = new int[]{dimentions[2], dimentions[3]};
//
//        for (int i = 0;i < yPos.length;i++) {
//            for (int j = 0;j < xPos.length;j++) {
//                if (isSolidTile(xa, ya, xPos[j], yPos[i])) {
//                    return true;
//                } else if (isSolidEntity(xa, ya, xPos[j], yPos[i])) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
