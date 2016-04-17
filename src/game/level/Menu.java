package game.level;

import game.InputHandler;
import game.entities.Player;
import game.gfx.Screen;
import game.level.tiles.Background;
import sokoban.Sokoban;
import sokoban.cells.Lantern;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static game.Game.*;

public class Menu extends Level {
    private final LevelManager levelManager;
    private final InputHandler input;
    private Background bg;
    private BufferedImage bgImage;


    private Color titleColor;
    private Color fontColor1;
    private Color fontColor2;


    private Font titleFont;
    private Font font;

    private String[] options = {
            "Start",
            "Test",
            "Make Board",
            "Help",
            "Quit"
    };
    private int currentChoice;


    public Menu(LevelManager levelManager, InputHandler input, String imagePath) {
        super(imagePath);
        this.levelManager = levelManager;
        this.input = input;

//        bg = new Background("/backgrounds/menubg.gif", 0);

        titleColor = new Color(246, 198, 77);
        fontColor1 = new Color(255, 220, 142);
        fontColor2 = new Color(255, 136, 18);


        titleFont = new Font("Old English Text MT", Font.BOLD, 120);

        font = new Font("OCR A EXTENDED", Font.PLAIN, 80);

        try {
            bgImage = ImageIO.read(getClass().getResourceAsStream("/backgrounds/menubg.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }


//        player = new Player(this, 0, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (bg != null) {
            bg.update();
        }


        if (input.up.isReleased()) {
//            AudioPlayer.MENUOPTION.play();
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = options.length-1;
            }
        }

        if (input.down.isReleased()) {
//            AudioPlayer.MENUOPTION.play();
            currentChoice++;
            if (currentChoice == options.length) {
                currentChoice = 0;
            }
        }

        if (input.enter.isReleased()) {
//            AudioPlayer.MENUSELECT.play();
            select();
        }
    }

    @Override
    public void draw(Graphics g, Screen screen) {
        if (bg != null) {
            bg.draw(g);
        }

        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, WIDTH*SCALE+20, HEIGHT*SCALE+20, null);
        }


        String title = "Sokoban";
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString(title, 350, 200);


        g.setFont(font);
        for (int i = 0;i < options.length;i++) {
            if (currentChoice == i) {
                g.setColor(fontColor2);
            } else {
                g.setColor(fontColor1);
            }
            g.drawString(options[i], 600-options[i].length()*20, 530+i*80);
        }
    }

    private void select() {
        if (currentChoice == 1) {
            levelManager.loadLevel(Levels.FIRST_LEVEL);
        }

        if (currentChoice == 0) {
            Level level = new Level(Sokoban.LEVEL);
            new Player(level, 0, 0);
            new Lantern(level, 20, 10, -0x00ff11);
//            new Lantern(level, 30, 10, -0x11);

//            new LightPoint(level, 40, 40, -0x661100, 20);

            levelManager.loadLevel(level);
        }

        if (currentChoice == 2) {
            // Add board
            String board = JOptionPane.showInputDialog("Enter Board");
            if (board != null) {
                levelManager.loadLevel(Sokoban.init(board));
            }
        }

        if (currentChoice == 3) {
            //help
        }

        if (currentChoice == 4) {
            System.exit(0);
        }
    }

}
