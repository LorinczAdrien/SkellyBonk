package main;

import entities.Player;
import game_logic.AudioManager;
import game_logic.input.KeyHandler;
import game_logic.panels.GameOverPanel;
import game_logic.panels.GamePanel;
import game_logic.panels.StartMenuPanel;
import game_logic.waves.WaveManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameFrame extends JFrame implements Runnable{
    // Logic
    private CardLayout cardLayout;
    private boolean startGame = false, gameOver = false, exited = false;

    // References
    private StartMenuPanel startMenuPanel;
    private GamePanel gamePanel;
    private GameOverPanel gameOverPanel;
    private KeyHandler keyHandler;
    private AudioManager audioManager;
    private Container container;

    public GameFrame() {
        this.initFrame();

        this.keyHandler = new KeyHandler();

        this.initAudio();
        this.initPanels();

        // More panel stuff
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Start game by running this thread
        Thread mainGameThread = new Thread(this);
        mainGameThread.start();
    }

    // Function that manages all the panels
    @Override
    public void run() {
        // Start Start menu
        this.startMenuPanel.startStartMenuPanel();
        while(!this.startGame) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Main frame - wait error");
                    throw new RuntimeException(e);
                }
            }
        }
        this.cardLayout.next(this.container);

        // Start game panel
        this.gamePanel.startGameThread();
        while(!this.gameOver) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Main frame - wait error");
                    throw new RuntimeException(e);
                }
            }
        }

        // Switching to Game Over screen
        this.cardLayout.last(this.container);
        this.gameOverPanel.startGameOverPanel();
    }

    public void notifyFrame() {
        synchronized (this) {
            notify();
        }
    }
    private void initFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.cardLayout = new CardLayout();
        this.setLayout(this.cardLayout);
        this.setResizable(false);
        this.setTitle("SkellyBonk");
    }

    private void initAudio() {
        // Set up audio
        this.audioManager = new AudioManager();
        Thread audioThread = new Thread(this.audioManager);
        audioThread.start();
    }

    private void initPanels() {
        this.container = this.getContentPane();

        // Start menu panel
        this.startMenuPanel = new StartMenuPanel(this);
        this.add(this.startMenuPanel);
        this.pack();

        // Game panel
        this.gamePanel = new GamePanel(this);
        this.add(this.gamePanel);

        // Gameover panel
        this.gameOverPanel = new GameOverPanel(this);
        this.add(this.gameOverPanel);
    }

    public static void main(String[] args) {
        GameFrame game = new GameFrame();
    }

    /* Getters */
    public GamePanel getGamePanel() { return this.gamePanel; }
    public WaveManager getWaveManager() { return this.gamePanel.getWaveManager(); }
    public KeyHandler getKeyHandler() { return this.keyHandler; }
    public AudioManager getAudioManager() { return this.audioManager; }
    public Player getPlayer() { return this.gamePanel.getPlayer(); }
    public File getChosenFile() { return this.startMenuPanel.getChosenFile(); }

    /* Setters */
    public void setStartGame(boolean state) { this.startGame = state; }
    public void setGameOver(boolean state) { this.gameOver = state; }
    public void setExited(boolean state) { this.exited = state; }
}
