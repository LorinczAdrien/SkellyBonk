package game_logic.panels;

import entities.Player;
import game_logic.AudioManager;
import game_logic.CollisionChecker;
import game_logic.input.KeyHandler;
import game_logic.input.MouseHandler;
import game_logic.ui.ArenaUIManager;
import game_logic.waves.WaveManager;
import main.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable {
    // Screen information
    private final int panelWidth = 1600, panelHeight = 900;

    // Sprite info
    private final int originalSpriteSize = 16, scale = 4, spriteSize = this.scale * this.originalSpriteSize;
    private BufferedImage backgroundImage;

    // Game Logic
    private final int gameFPS = 60;
    private final double tickInterval = 1_000_000_000 / (double) this.gameFPS;
    private boolean gameOver = false;
    private final GameFrame gameFrame;
    private KeyHandler keyHandler;
    private MouseHandler mouseHandler;
    private Rectangle barrierUp, barrierLeft, barrierDown, barrierRight, barrierMiddle;
    private Rectangle spawnBoundingBox;
    private WaveManager waveManager;
    private ArenaUIManager arenaUIManager;
    private CollisionChecker collisionChecker;
    private final AudioManager audioManager;

    // Entities
    private Player player;

    // Logic
    private Thread gameThread;

    public GamePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.audioManager = this.gameFrame.getAudioManager();

        this.initPanel();
        this.initVariables();
        this.initGameLogic();
        this.addListeners();
    }

    private void initGameLogic() {
        // Game logic
        this.keyHandler = this.gameFrame.getKeyHandler();
        this.mouseHandler = new MouseHandler();

        // Entities
        this.player = new Player(this);

        // More game logic
        this.collisionChecker = new CollisionChecker(this.player);
        this.waveManager = new WaveManager(this, this.collisionChecker);
        this.arenaUIManager = new ArenaUIManager(this, this.player);
    }

    private void initPanel() {
        this.setPreferredSize(new Dimension(this.panelWidth, this.panelHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
    }

    private void initVariables() {
        // Barriers at sides and middle
        int padding = 75;
        this.barrierUp = new Rectangle(0, 0, this.panelWidth, padding);
        this.barrierLeft = new Rectangle(0, 0, padding, this.panelHeight);
        this.barrierDown = new Rectangle(0, this.panelHeight - padding, this.panelWidth, padding);
        this.barrierRight = new Rectangle(this.panelWidth - padding, 0, padding, this.panelHeight);
        int middleSize = 130;
        this.barrierMiddle = new Rectangle(this.panelWidth / 2 - middleSize / 2, this.panelHeight / 2 - middleSize / 2, middleSize, middleSize);

        // The bounding box, where the monsters and player spawns
        int spawnerPadding = padding + this.spriteSize;         // Spawner needs more padding because it can't spawn enemies in wals
        this.spawnBoundingBox = new Rectangle(spawnerPadding, spawnerPadding, this.panelWidth - 2 * spawnerPadding, this.panelHeight - 2 * spawnerPadding);

        // Sprites
        try {
            this.backgroundImage = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/game/game_panel_background.png")));
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Error: Game panel background not found!");
        }
    }

    private void addListeners() {
        // Add key listener to this panel
        this.addKeyListener(this.keyHandler);
        this.addMouseListener(this.mouseHandler);
    }

    // Starts the game
    public void startGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    @Override
    public void run() {
        // Setting up the game loop
        double nextTickTime = System.nanoTime() + tickInterval;
        this.requestFocus();

        while(gameThread != null && !this.gameOver) {         // While the game thread object exists
//            System.out.println("Debug: Game is running...");
            // Update
            this.update();

            // Repaint
            this.repaint();

            // Next tick
            this.nextTick(nextTickTime);
            nextTickTime += tickInterval;
        }

        this.gameFrame.setGameOver(true);
        this.gameFrame.notifyFrame();
    }

    private void nextTick(double nextTickTime) {
        double sleepMilliseconds = (nextTickTime - System.nanoTime()) / 1_000_000;  // Nanoseconds -> Milliseconds

        if(sleepMilliseconds > 0) {     // If we need to sleep until the next tick
            try {
                Thread.sleep((long) sleepMilliseconds);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                System.out.println("Error: Thread could not sleep until next tick!");
            }
        }
    }

    public void update() {
        // Update UI
        this.arenaUIManager.updateUI();

        // Update player
        this.player.update();

        // Update wave spawning
        this.waveManager.update();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D)graphics;
        // First we draw the background for the panel
        graphics2D.drawImage(this.backgroundImage, 0, 0, this.panelWidth, this.panelHeight, null);

        // Draw UI
        this.arenaUIManager.drawUI(graphics2D);

        // Draw player
        this.player.draw(graphics2D);

        // Update enemies via wave manager
        this.waveManager.draw(graphics2D);
    }

    // Utils
    public void drawRectangle(Rectangle rect, Graphics2D graphics2D) {
        graphics2D.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    public void shootGun() { this.audioManager.shootGun(); }
    public void playerDamaged() { this.audioManager.playerDamaged(); }

    public void updateChoosenFile(File file) { this.waveManager.setWavesFile(file);}

    /* Getters */
    public boolean isGameOver() { return this.gameOver; }
    public int getPanelWidth() { return this.panelWidth; }
    public int getPanelHeight() { return this.panelHeight; }
    public int getSpriteSize() { return this.spriteSize; }
    public int getOriginalSpriteSize() { return this.originalSpriteSize; }
    public KeyHandler getKeyHandler() { return this.keyHandler; }
    public MouseHandler getMouseHandler() { return this.mouseHandler; }
    public CollisionChecker getCollisionChecker() { return this.collisionChecker; }
    public WaveManager getWaveManager() { return  this.waveManager; }
    public Player getPlayer() { return this.player; }
    public Rectangle getBarrierUp() { return this.barrierUp; }
    public Rectangle getBarrierLeft() { return this.barrierLeft; }
    public Rectangle getBarrierDown() { return this.barrierDown; }
    public Rectangle getBarrierRight() { return this.barrierRight; }
    public Rectangle getBarrierMiddle() { return this.barrierMiddle; }
    public Rectangle getSpawnBoundingBox() { return this.spawnBoundingBox; }

    /* Setters */
    public void setGameOver(boolean state) { this.gameOver = state; }
}
