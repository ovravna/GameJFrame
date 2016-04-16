package game.level;

import game.InputHandler;
import game.entities.Player;
import game.gfx.Screen;

import java.awt.*;

public class Menu extends Level {
    private final LevelManager levelManager;
    private final InputHandler input;
    Player player;

    private Color titleColor;
    private Font titleFont;
    private Font font;
    private Graphics2D g;

    private String[] options = {
            "Start",
            "Help",
            "Quit"
    };
    private int currentChoice;


    public Menu(LevelManager levelManager, InputHandler input, String imagePath) {
        super(imagePath);
        this.levelManager = levelManager;
        this.input = input;

        titleColor = new Color(128, 0, 0);
        titleFont = new Font("Century Gothic", Font.BOLD, 120);

        font = new Font("Arial", Font.PLAIN, 80);



//        player = new Player(this, 0, 0, input);
    }

    @Override
    public void tick() {
        super.tick();

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
        String title = "Dragon Tale";
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString(title, 250, 200);


        g.setFont(font);
        for (int i = 0;i < options.length;i++) {
            if (currentChoice == i) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 500, 400+i*80);
        }
    }

    private void select() {
        if (currentChoice == 0) {
            levelManager.loadLevel(Levels.FIRST_LEVEL);
        }
        if (currentChoice == 1) {
            // help
        }
        if (currentChoice == 2) {
            System.exit(0);
        }
    }

}
