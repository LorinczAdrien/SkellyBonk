package game_logic.waves;

import entities.monsters.BatMonster;
import entities.monsters.DragonMonster;
import entities.monsters.GhostMonster;
import entities.monsters.SlimeMonster;
import game_logic.CollisionChecker;
import game_logic.spawners.MonsterSpawner;
import game_logic.panels.GamePanel;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class WaveManager {
    // Reference to game panel
    private final GamePanel gamePanel;
    private final CollisionChecker collisionChecker;

    // Spawners
    private MonsterSpawner slimeSpawner, batSpawner, ghostSpawner, dragonSpawner;

    // Logic
    private boolean hasMoreWaves = true;
    private int numberOfWaves, currentWaveCount = 0, tickCounter = 0, tickToReach = 0;
    private final Random rand = new Random();
    private File wavesFile;
    private StringTokenizer tokenizer;
    private ArrayList<String> wavesInfo;

    public WaveManager(GamePanel gamePanel, CollisionChecker collisionChecker) {
        this.gamePanel = gamePanel;
        this.collisionChecker = collisionChecker;

        // Init variables
        this.initVariables();
    }

    private void initVariables() {
        // Spawners
        this.slimeSpawner = new MonsterSpawner(this.gamePanel, new SlimeMonster(this.gamePanel, 0, 0));
        this.collisionChecker.setSlimeSpawner(this.slimeSpawner);

        this.batSpawner = new MonsterSpawner(this.gamePanel, new BatMonster(this.gamePanel, 0, 0));
        this.collisionChecker.setBatSpawner(this.batSpawner);

        this.ghostSpawner = new MonsterSpawner(this.gamePanel, new GhostMonster(this.gamePanel, 0, 0));
        this.collisionChecker.setGhostSpawner(this.ghostSpawner);

        this.dragonSpawner = new MonsterSpawner(this.gamePanel, new DragonMonster(this.gamePanel, 0, 0));
        this.collisionChecker.setDragonSpawner(this.dragonSpawner);

        // Others
        this.wavesInfo = new ArrayList<>();
    }

    private void wavesFromFile(File file) {
        this.wavesInfo.clear();
        try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()) {
                this.wavesInfo.add(reader.nextLine());
            }

        } catch (FileNotFoundException exception) {
            System.out.println("File not found!");
            throw new RuntimeException();
        }

        // Get number of waves
        this.numberOfWaves = this.wavesInfo.size();
    }

    private void updateWaves() {
        // If one of the spawners currently isn't spawning then we wait the given seconds
        if(!this.isSpawning()) {
            this.tickCounter++;

            // If we need to wait for the next wave to spawn we wait
            if (this.tickCounter >= this.tickToReach) {
                if (tokenizer.hasMoreTokens()) {
                    this.nextSpawnSetup();
                } else {
                    this.currentWaveCount++;

                    // If we have waves left, or we're done
                    if (this.currentWaveCount == this.numberOfWaves) {
                        this.hasMoreWaves = false; this.currentWaveCount--;
                    }
                    else {
                        tokenizer = new StringTokenizer(this.wavesInfo.get(this.currentWaveCount), " ");
                    }
                }
            }
        }
    }

    private void nextSpawnSetup() {
        // We get the next token
        String monsterName = tokenizer.nextToken();
        String interval = tokenizer.nextToken();
        String secondsToWait = tokenizer.nextToken();

        // We randomly generate how many monsters we will have given the interval
        StringTokenizer intervalTokenizer = new StringTokenizer(interval, "-");
        int intervalStart = Integer.parseInt(intervalTokenizer.nextToken()), intervalEnd = Integer.parseInt(intervalTokenizer.nextToken());
        int monsterSpawnCount = rand.nextInt(intervalStart, intervalEnd);

        // We spawn monsters of the given type
        if(monsterName.equals("slime")) this.slimeSpawner.spawnMonsters(monsterSpawnCount);
        else if (monsterName.equals("bat")) this.batSpawner.spawnMonsters(monsterSpawnCount);
        else if (monsterName.equals("ghost")) this.ghostSpawner.spawnMonsters(monsterSpawnCount);
        else if (monsterName.equals("dragon")) this.dragonSpawner.spawnMonsters(monsterSpawnCount);
        else {
            System.out.println("Wave manager: Error -> Monster:" + monsterName + " doesn't exits in the database");
            throw new RuntimeException();
        }

        // We wait the given seconds until we spawn the next one
        this.tickCounter = 0;
        this.tickToReach = Integer.parseInt(secondsToWait) * 60;    // one tick is 1/60-th of a second
    }

    public void update() {
//        if(keyHandler.spawnSlime) {
//            this.slimeSpawner.spawnMonster();
//            keyHandler.spawnSlime = false;
//        }
//
//        if(keyHandler.spawnBat) {
//            this.batSpawner.spawnMonster();
//            keyHandler.spawnBat = false;
//        }
//
//        if(keyHandler.spawnGhost) {
//            this.ghostSpawner.spawnMonster();
//            keyHandler.spawnGhost = false;
//        }
//
//        if(keyHandler.spawnDragon) {
//            this.dragonSpawner.spawnMonster();
//            keyHandler.spawnDragon = false;
//        }

        // We update the wave spawning if we need to
        if(this.hasMoreWaves) this.updateWaves();

        // Update spawners (also enemies)
        this.updateSpawners();

        // If we won't spawn more waves and all enemies are dead -> We communicate it to the game panel
        if(!this.hasMoreWaves && (this.aliveMonsterCount() == 0)) this.gamePanel.setGameOver(true);
    }

    private void updateSpawners() {
        // Update the spawners
        this.slimeSpawner.update();
        this.batSpawner.update();
        this.ghostSpawner.update();
        this.dragonSpawner.update();
    }

    public void draw(Graphics2D graphics2D) {
        this.slimeSpawner.draw(graphics2D);
        this.batSpawner.draw(graphics2D);
        this.ghostSpawner.draw(graphics2D);
        this.dragonSpawner.draw(graphics2D);
    }

    /* Getters */
    public boolean hasMoreWaves() { return this.hasMoreWaves; }
    public boolean isSpawning() { return this.slimeSpawner.isSpawning() || this.batSpawner.isSpawning() ||
                                         this.ghostSpawner.isSpawning() || this.dragonSpawner.isSpawning(); }
    public int aliveMonsterCount() { return (
                                            this.slimeSpawner.getMonsters().size() +
                                            this.batSpawner.getMonsters().size() +
                                            this.ghostSpawner.getMonsters().size() +
                                            this.dragonSpawner.getMonsters().size()
                                            ); }
    public int getCurrentWaveCount() { return this.currentWaveCount; }
    public int getNumberOfWaves() { return this.numberOfWaves; }

    /* Setters */
    public void setWavesFile(File file) {
        this.wavesFile = file;
        this.wavesFromFile(this.wavesFile);
        this.tokenizer = new StringTokenizer(this.wavesInfo.get(this.currentWaveCount), " ");
        this.nextSpawnSetup();
    }
}
