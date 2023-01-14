package game_logic.panels;

import entities.Player;
import game_logic.AudioManager;
import game_logic.waves.WaveManager;
import main.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GameOverPanel extends JPanel implements Runnable {
    // Screen information
    private final int panelWidth = 1600, panelHeight = 900;
    private Thread gameOverThread;

    // References
    private final GameFrame gameFrame;
    private final WaveManager waveManager;
    private final AudioManager audioManager;
    private final Player player;

    // Sprites
    private BufferedImage background;
    private ArrayList<BufferedImage> skellyAnimLeft, skellyAnimRight;

    // Labels
    private final JLabel gameOverLabel = new JLabel("Game Over"), scoreLabel = new JLabel("Score"), wavesLabel = new JLabel("Waves survived");
    private JLabel scoreCountLabel = new JLabel("000000"), wavesCountLabel = new JLabel("000000");

    // Game Logic
    private boolean exit = false;
    private final int gameFPS = 60;
    private int spriteInd = 0, spriteCounter = 0;
    private final double tickInterval = 1_000_000_000 / (double) this.gameFPS;

    public GameOverPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.audioManager = this.gameFrame.getAudioManager();
        this.waveManager = this.gameFrame.getWaveManager();
        this.player = this.gameFrame.getPlayer();

        this.initPanel();
        this.initVariables();
    }

    private void initPanel() {
        this.setPreferredSize(new Dimension(this.panelWidth, this.panelHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);

        this.gameOverThread = new Thread(this);
    }

    private void initVariables() {
        // Labels
        Font newFont = new Font(this.gameOverLabel.getFont().getFontName(), Font.BOLD, 100);
        this.initLabel(this.gameOverLabel, newFont, new Point(500, 100));

        Font newFont2 = new Font(newFont.getFontName(), Font.BOLD, 50);
        this.initLabel(this.scoreLabel, newFont2, new Point(500, 400));
        this.initLabel(this.scoreCountLabel, newFont2, new Point(1000, 400));
        this.initLabel(this.wavesLabel, newFont2, new Point(500, 500));
        this.initLabel(this.wavesCountLabel, newFont2, new Point(1000, 500));

        this.initSprites();
    }

    private void initSprites() {
        // Background
        try {
            this.background = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/gameUI/background.png")));
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Error: GameUI background not found!");
        }

        this.skellyAnimLeft = new ArrayList<>();
        this.skellyAnimRight = new ArrayList<>();

        this.loadOneAnimation(this.skellyAnimLeft, "left");
        this.loadOneAnimation(this.skellyAnimRight, "right");
    }

    private void loadOneAnimation(ArrayList<BufferedImage> images, String type) {
        String className = getClass().getSimpleName().toLowerCase();    // We use the class name and type to find the corresponding directory
        try {
            File path = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "images" + File.separator + "skelly" + File.separator + type);
            if(path.exists() && path.isDirectory()) {
                File[] allFiles = path.listFiles();
                if(allFiles != null) {
                    for (File allFile : allFiles) images.add(ImageIO.read(allFile));
                } else System.out.println("Error (" + className + "): " + path + " directory is null!");
            }
            else System.out.println("Error (" + className + "): " + path + " doesn't exits or isn't directory");
        } catch (IOException exception) {
            System.out.println("Error: Couldn't read " + type + " sprites");
        }
    }

    private void initLabel(JLabel label, Font newFont, Point pos) {
        this.add(label);
        label.setForeground(Color.white); label.setFont(newFont);
        Dimension size = label.getPreferredSize();
        label.setBounds(0, 0, size.width, size.height);
        label.setLocation(pos);
    }

    public void update() {
        // Update labels
        this.scoreCountLabel.setText("" + this.player.getScore());
        if(this.waveManager.getCurrentWaveCount() > 0)
            this.wavesCountLabel.setText("" + (this.waveManager.getCurrentWaveCount() + 1) + " / " + this.waveManager.getNumberOfWaves());
        else
            this.wavesCountLabel.setText("" + "0 / " + this.waveManager.getNumberOfWaves());
    }

    @Override
    public void paintComponent(Graphics graphics2D) {
        super.paintComponent(graphics2D);

        // Draw background
        graphics2D.drawImage(this.background, 0, 0, this.panelWidth, this.panelHeight, null);

        // Draw skelly animations
        this.spriteCounter++;
        if(this.spriteCounter > 15) {
            this.spriteCounter = 0;
            this.spriteInd = (this.spriteInd + 1) % 2;  // [0,1]
        }
        graphics2D.drawImage(this.skellyAnimLeft.get(this.spriteInd), 200, 300, 256, 256, null);
        graphics2D.drawImage(this.skellyAnimRight.get(this.spriteInd), 1200, 300, 256, 256, null);
    }

    @Override
    public void run() {
        // Setting up the game loop
        double nextTickTime = System.nanoTime() + tickInterval;

        while(this.gameOverThread != null && !this.exit) {         // While the game thread object exists
            // Update
            this.update();

            // Repaint
            this.repaint();

            // Next tick
            this.nextTick(nextTickTime);
            nextTickTime += tickInterval;
        }
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

    public void startGameOverPanel() {
        this.gameOverThread.start();
    }
}
