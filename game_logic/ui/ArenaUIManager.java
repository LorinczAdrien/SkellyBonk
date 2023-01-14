package game_logic.ui;

import entities.Player;
import game_logic.panels.GamePanel;
import game_logic.waves.WaveManager;

import javax.swing.*;
import java.awt.*;

public class ArenaUIManager {
    // Contains references to game objects
    private final GamePanel gamePanel;
    private final WaveManager waveManager;
    private final Player player;

    // UI elements
    private final JLabel healthLabel = new JLabel("Health"), xpLabel = new JLabel("XP"), timeLabel = new JLabel("Time"), waveLabel = new JLabel("Wave"), scoreLabel = new JLabel("Score");
    private JLabel healthLabelCount, xpLabelCount, timeLabelCount, waveLabelCount, scoreLabelCount;

    // Helper stuff
    private long time;

    public ArenaUIManager(GamePanel gamePanel, Player player) {
        this.gamePanel = gamePanel;
        this.waveManager = this.gamePanel.getWaveManager();
        this.player = player;

        // Init
        this.initVariables();
    }

    private void initVariables() {
        // Init time
        this.time = System.nanoTime();

        // Initialize labels and such
        this.healthLabelCount = new JLabel("0000000");
        this.xpLabelCount = new JLabel("10000");
        this.timeLabelCount = new JLabel("00:00");
        this.waveLabelCount = new JLabel("00000");
        this.scoreLabelCount = new JLabel("000000");

        // Adjust labels
        Font newFont = new Font(this.healthLabel.getFont().getFontName(), Font.BOLD, 30);
        this.initLabel(this.healthLabel, newFont, new Point(130, 20));
        this.initLabel(this.healthLabelCount, newFont, new Point(250, 20));
        this.initLabel(this.xpLabel, newFont, new Point(400, 20));
        this.initLabel(this.xpLabelCount, newFont, new Point(450, 20));
        int timeX = this.gamePanel.getPanelWidth() / 2 - 40;
        this.initLabel(this.timeLabel, newFont, new Point(timeX + 5, 5));
        this.initLabel(this.timeLabelCount, newFont, new Point(timeX, 40));
        this.initLabel(this.waveLabel, newFont, new Point(1300, 20));
        this.initLabel(this.waveLabelCount, newFont, new Point(1400, 20));
        this.initLabel(this.scoreLabel, newFont, new Point(1100, 20));
        this.initLabel(this.scoreLabelCount, newFont, new Point(1200, 20));
    }

    private void initLabel(JLabel label, Font newFont, Point pos) {
        this.gamePanel.add(label);
        label.setForeground(Color.white); label.setFont(newFont);
        Dimension size = label.getPreferredSize();
        label.setBounds(0, 0, size.width, size.height);
        label.setLocation(pos);
    }

    public void updateUI() {
        this.healthLabelCount.setText("" + this.player.getHealth() + " / " + this.player.getMaxHealth() );
        this.xpLabelCount.setText("" + this.player.getXp());

        // Calculate seconds passed:
        long secondsPassed = (System.nanoTime() - this.time) / 1_000_000_000;
        long minutesPassed = secondsPassed / 60;
        secondsPassed -= minutesPassed * 60;

        String timeText = new String();
        if(minutesPassed == 0) timeText += "00";
        else if(minutesPassed < 10) timeText += "0";
        if(minutesPassed != 0) timeText += minutesPassed;
        timeText += ":";

        if(secondsPassed == 0) timeText += "00";
        else if(secondsPassed < 10) timeText += "0";
        if(secondsPassed != 0) timeText += secondsPassed;
        this.timeLabelCount.setText(timeText);

        // Update score
        this.scoreLabelCount.setText("" + this.player.getScore());

        // Update wave count
        this.waveLabelCount.setText("" + (this.waveManager.getCurrentWaveCount() + 1) + " / " + this.waveManager.getNumberOfWaves());
    }

    public void drawUI(Graphics2D graphics2D) {

    }
}
