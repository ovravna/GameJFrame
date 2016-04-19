import game.Game;
import sokoban.Sokoban;

import java.applet.Applet;
import java.awt.*;
public class SokobanLauncher extends Applet {

    private static Game game = new Game("Sokoban");

    @Override
    public void init() {
        setLayout(new BorderLayout());
        add(game, BorderLayout.CENTER);
        setMaximumSize(Game.DIMENSIONS);
        setMinimumSize(Game.DIMENSIONS);
        setPreferredSize(Game.DIMENSIONS);

        game.isApplet = true;
    }

    @Override
    public void start() {
        game.start();
    }

    @Override
    public void stop() {
        game.stop();
    }

    public static void main(String[] args) {
        new Sokoban();
    }
}
