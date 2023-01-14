package game_logic.panels;

import main.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;

public class StartMenuPanel extends JPanel implements Runnable, ActionListener {
    // Screen information
    private final int panelWidth = 1600, panelHeight = 900;
    private Thread startThread;

    // References
    private final GameFrame gameFrame;

    // Sprites
    private BufferedImage background;
    private ArrayList<BufferedImage> skellyAnimLeft, skellyAnimRight;

    // Buttons and labels
    private JButton startButton, chooseButton;
    private JLabel difficultyLabel;

    // Game Logic
    private File chosenFile;
    private JFileChooser fileChooser;
    private boolean start = false;
    private int spriteInd = 0, spriteCounter = 0;
    private final int gameFPS = 60;
    private final double tickInterval = 1_000_000_000 / (double) this.gameFPS;

    public StartMenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        this.initPanel();
        this.initVariables();
    }

    private void initPanel() {
        this.setPreferredSize(new Dimension(this.panelWidth, this.panelHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);

        this.startThread = new Thread(this);
    }

    private void initVariables() {
        this.initButtonsLabels();
        this.initSprites();

        // File chooser
        this.fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        File currentDirectory = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "waves");   // Find current directory
        this.fileChooser.setCurrentDirectory(currentDirectory);             // We open here

        // Default file
        String sep = File.separator;
        this.chosenFile = new File(System.getProperty("user.dir") + sep + "src" + sep + "waves" + sep + "normal.txt");
    }

    private void initButtonsLabels() {
        // Buttons
        this.startButton = new JButton("Start");
        this.chooseButton = new JButton("Choose difficulty");
        Font newFont = new Font(new JLabel().getFont().getFontName(), Font.BOLD, 30);
        this.startButton.setFont(newFont); this.chooseButton.setFont(newFont);
        this.add(this.startButton); this.add(this.chooseButton);
        this.startButton.setBounds(650, 300, 350, 150); this.chooseButton.setBounds(650, 500, 350, 150);
        this.startButton.addActionListener(this); this.chooseButton.addActionListener(this);

        // Labels
        this.difficultyLabel = new JLabel("Difficulty normal");
        this.difficultyLabel.setFont(newFont); this.difficultyLabel.setForeground(Color.WHITE);
        this.add(this.difficultyLabel);
        this.difficultyLabel.setBounds(700, 650, 350, 150);
    }

    private void initSprites() {
        // Background
        try {
            this.background = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/gameUI/background_with_logo.png")));
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


    public void update() {
        // Update labels
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

        while(this.startThread != null && !this.start) {         // While the game thread object exists
            // Update
            this.update();

            // Repaint
            this.repaint();

            // Next tick
            this.nextTick(nextTickTime);
            nextTickTime += tickInterval;
        }

        // Update with chosen file (or default)
        this.gameFrame.getGamePanel().updateChoosenFile(this.chosenFile);

        this.gameFrame.setStartGame(true);
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

    public void startStartMenuPanel() {
        this.startThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == this.startButton) {
            this.start = true;
        } else if(event.getSource() == this.chooseButton) {
            int rCode = this.fileChooser.showOpenDialog(null);

            // If user chose a file
            if(rCode == JFileChooser.APPROVE_OPTION) {
                this.chosenFile = this.fileChooser.getSelectedFile();

                // Update label
                String fileName = this.chosenFile.getName();
                StringTokenizer dotTokens = new StringTokenizer(fileName, ".");
                this.difficultyLabel.setText("Difficulty " + dotTokens.nextToken());
            } else {
                System.out.println("Debug: User didn't choose a file!");
            }
        }
    }

    /* Getters */
    public File getChosenFile() { return this.chosenFile; }
}
