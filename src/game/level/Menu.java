package game.level;

import game.InputHandler;
import game.entities.Player;

public class Menu extends Level {
    private final InputHandler input;
    Player player;

    public Menu(InputHandler input,  String imagePath) {
        super(imagePath);
        this.input = input;
        System.out.println("kake");
        player = new Player(this, 0, 0, input);
    }




}
