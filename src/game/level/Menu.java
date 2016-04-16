package game.level;

import game.InputHandler;
import game.gfx.Screen;
import game.level.tiles.Background;
import sokoban.Sokoban;

import javax.swing.*;
import java.awt.*;

public class Menu extends Level {
    private final LevelManager levelManager;
    private final InputHandler input;
        private Background bg;


    private Color titleColor;
    private Color fontColor1;
    private Color fontColor2;


    private Font titleFont;
    private Font font;

    private String[] options = {
            "Start",
            "Add Board",
            "Help",
            "Quit"
    };
    private int currentChoice;


    public Menu(LevelManager levelManager, InputHandler input, String imagePath) {
        super(imagePath);
        this.levelManager = levelManager;
        this.input = input;

        bg = new Background("/backgrounds/menubg.gif", 0);
//        bg.setVector(-1, 0);

        titleColor = new Color(246, 198, 77);
        fontColor1 = new Color(247, 246, 184);
        fontColor2 = new Color(255, 136, 18);


        titleFont = new Font("Century Gothic", Font.BOLD, 120);



        font = new Font("Arial", Font.PLAIN, 80);



//        player = new Player(this, 0, 0, input);
    }

    @Override
    public void tick() {
        super.tick();
        bg.update();


        if (input.up.isReleased()) {
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = options.length-1;
            }
        }

        if (input.down.isReleased()) {
            currentChoice++;
            if (currentChoice == options.length) {
                currentChoice = 0;
            }
        }

        if (input.enter.isReleased()) {
            select();
        }
    }

    @Override
    public void draw(Graphics g, Screen screen) {
        bg.draw(g);

        String title = "Sokoban";
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString(title, 350, 200);


        g.setFont(font);
        for (int i = 0;i < options.length;i++) {
            if (currentChoice == i) {
                g.setColor(fontColor1);
            } else {
                g.setColor(fontColor2);
            }
            g.drawString(options[i], 600-options[i].length()*20, 400+i*80);
        }
    }

    private void select() {
        if (currentChoice == 0) {
            levelManager.loadLevel(Levels.FIRST_LEVEL);
        }
        if (currentChoice == 1) {
            // Add board
            String board = JOptionPane.showInputDialog("Enter Board");
            if (board != null) {
                levelManager.loadLevel(Sokoban.init(board));
            }
        }

        if (currentChoice == 2) {
            //help
        }

        if (currentChoice == 3) {
            System.exit(0);
        }
    }

}
