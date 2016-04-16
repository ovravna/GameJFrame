package game;

import game.entities.Player;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;
import game.level.LevelManager;
import sokoban.cells.Goal;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends Canvas implements Runnable {

    public static final long serialVersionUID = 1L;

    public static final int WIDTH = 120;
    public static final int HEIGHT = WIDTH/12*9;
    public static final int SCALE = 10;
    public static String name;
    private JFrame frame;

    public boolean running = true;
    public int tickCount = 0;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[6*6*6];
    private Screen screen;

    private LevelManager levelManager;
    public static InputHandler input;
    public Level level;
    public Player player;
    public List<GameListener> listeners = new ArrayList<>();

    private boolean daylightCycle = true;

//    public static void setLight(int light) {
//        Game.light = light;
//    }

    private static int light = -0xdf;
    private int cycleTime = 20;
    private int smoothRise = 0;
    private boolean wonFlag;

    //    private int x = 0;


//    public Game() {

    public Game(GameListener... listeners) {
        this("Game", listeners);
    }

    public Game(String name, GameListener... listeners) {
        setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        this.name = name;
//        this.daylightCycle = false;
//        this.light = 0;


        frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addListener(listeners);
        setScreen(new Screen(WIDTH, HEIGHT, new SpriteSheet("/8x8font.png")));
        setInputHandler(new InputHandler(this));

        setLevel(new Level("/levels/sokoban_test.png"));

        levelManager = new LevelManager(input, level);


//        setPlayer(new Player(level, 0, 0, input, "Player"));


//        this.screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/8x8font.png"));
        screen.sheet.setFontLine(1);
        screen.sheet.setPlayerLine(10);


    }

    public void setDaylightCycle(boolean daylightCycle) {
        this.daylightCycle = daylightCycle;
    }

    public static void setLight() {
        setLight(-0xdf);
    }

    public static void setLight(Integer light) {
        if (light == null) {
            setLight();
            return;
        }
        Screen.filterColor = light;
    }

    public void setLighting(boolean lighting) {
        screen.setLightOn(lighting);
    }

    public void setCycleTime(int cycleTime) {
        this.cycleTime = cycleTime;
    }

    public static int getLight() {
        return Game.light;
    }

    private void addListener(GameListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    private void setPlayer(Player player) {
        this.player = player;
        listeners.forEach(n -> n.newPlayer(player));

    }

    public void action() {
        listeners.forEach(GameListener::action);
    }


    private void setScreen(Screen screen) {
        this.screen = screen;
        listeners.forEach(n -> n.newScreen(screen));
    }

    public void setInputHandler(InputHandler input) {
        this.input = input;
        listeners.forEach(n -> n.newInputHandler(input));
    }

    private void setLevel(Level level) {
        this.level = level;
        listeners.forEach(n -> n.newLevel(level));
    }

    public void init() {
//        int index = 0;
//
//        for (int r = 0;r < 6;r++) {
//            for (int g = 0;g < 6;g++) {
//                for (int b = 0;b < 6;b++) {
//                    int rr = (r*255/5);
//                    int gg = (g*255/5);
//                    int bb = (b*255/5);
//
//                    colors[index++] = rr << 16 | gg << 8 | bb;
//                }
//            }
//        }


//        level.addEntities(player);


        // JOptionPane.showInputDialog(this, "Enter username")
    }

    public synchronized void start() {
        running = true;
        new Thread(this).start();
    }

    private synchronized void stop() {
        running = false;
    }


    public long globalTime;

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1_000_000_000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        init();

        long now;
        boolean shouldRender;
        while (running) {
            now = System.nanoTime();
            delta += (now-lastTime)/nsPerTick;
            lastTime = now;
            shouldRender = true;

            while (delta >= 1) {
                ticks++;
                tick();
                delta--;
                shouldRender = true;
                if (daylightCycle) {
                    globalTime++;
                    screen.setFilter(globalTime, cycleTime, true, 0);
                }

                if (smoothRise > 0) {
                    globalTime++;
                    if (screen.setFilter(globalTime, cycleTime, smoothRise == 1 ? true:false, 0)) {
                        smoothRise = 0;
                    }

                }
            }

//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            if (shouldRender) {
                frames++;
                render();


            }

            if (System.currentTimeMillis()-lastTimer >= 1000) {
                lastTimer += 1000;
                frame.setTitle(String.format("  %3d ticks | %3d fps \n", ticks, frames));
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
        levelManager.currentLevel().tick();


        if (Goal.goals.stream().anyMatch(Goal::isWon) && !wonFlag) {
            smoothRise = 1;
            setCycleTime(1);
            wonFlag = true;
        }

    }





    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        int xOffset = 0;
        int yOffset = 0;

        if (levelManager.currentLevel().player != null) {
            xOffset = levelManager.currentLevel().player.x -screen.width/2;
            yOffset = levelManager.currentLevel().player.y -screen.height/2;

        }

        levelManager.currentLevel().renderTiles(screen, xOffset, yOffset);

        levelManager.currentLevel().renderEntities(screen);

        for (int y = 0;y < screen.height;y++) {
            for (int x = 0;x < screen.width;x++) {
                int colorCode = screen.pixels[x+y*WIDTH];
                pixels[x+y*WIDTH] = colorCode;
            }
        }


        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.PINK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

//        g.setFont(new Font("OCR A Extended", Font.BOLD, 40));
//        g.setColor(Color.BLACK);
//        g.drawString("kake []", xOffset-2, yOffset-10);

        g.dispose();
        bs.show();

    }

    public static void main(String... args) {
        new Game().start();
    }
}
