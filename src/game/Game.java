package game;

import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;
import game.level.LevelManager;
import game.level.Lighting;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {

    public static final long serialVersionUID = 1L;

    public static final int WIDTH = 120;
    public static final int HEIGHT = WIDTH/12*9;
    public static final int SCALE = 10;
    private static String name;
    private JFrame frame;

    private boolean running = true;
    public int tickCount = 0;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private Screen screen;
    private LevelManager levelManager;
    public static final Dimension DIMENSIONS = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);

    private int frames;
    public static boolean META_DATA = false;
    public boolean isApplet = false;
    private Thread thread;
    private int ticks;

    public Game() {
        this("Game", null);
    }

    public Game(String name) {
        this(name, null);
    }

    public Game(String name, Level level) {
        Game.name = name;

        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/8x8font.png"));
        screen.sheet.setFontLine(1);
        screen.sheet.setPlayerLine(10);

        levelManager = new LevelManager(screen, new InputHandler(this), level);
    }

    public void init() {
        setMinimumSize(Game.DIMENSIONS);
        setMaximumSize(Game.DIMENSIONS);
        setPreferredSize(Game.DIMENSIONS);

        frame = new JFrame(name);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.start();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();


    }

    public synchronized void stop() {
        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1_000_000_000D/60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
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
            }

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis()-lastTimer >= 1000) {
                lastTimer += 1000;

//                int x = 0;
//                int y = 0;
//                if (levelManager.currentLevel().getPlayer() != null) {
//                    x = levelManager.currentLevel().getPlayer().x;
//                    y = levelManager.currentLevel().getPlayer().y;
//                }
//                frame.setTitle(String.format("%s FPS-%s Ticks-%s", name, frames, ticks));
                this.frames = frames;
                this.ticks = ticks;
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
        levelManager.tick();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        int xOffset = 0;
        int yOffset = 0;

        if (levelManager.currentLevel().getPlayer() != null) {
            xOffset = levelManager.currentLevel().getPlayer().x-screen.width/2;
            yOffset = levelManager.currentLevel().getPlayer().y-screen.height/2;

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
        levelManager.draw(g);

        if (META_DATA) {
            String s = String.format("Ticks-%-4s FPS-%-4s LS-%s", ticks, frames, Lighting.sources);

            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString(s, 5, 25);
        }

        g.dispose();
        bs.show();

    }

    public static void main(String... args) {
        new Game().start();
    }
}
