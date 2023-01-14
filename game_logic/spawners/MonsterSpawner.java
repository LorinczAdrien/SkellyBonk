package game_logic.spawners;

import entities.LivingEntity;
import entities.monsters.Monster;
import game_logic.panels.GamePanel;

import java.awt.*;
import java.util.ArrayList;

public class MonsterSpawner extends Spawner {
    // The monster it spawns
    protected Monster monster;
    protected ArrayList<Monster> monsters;      // The monsters it spawned
    protected ArrayList<Thread> monsterThreads;

    // Logic
    protected int needToSpawnCount = 0;
    protected int ticksBetweenSpawns = 30, tickCounter = 0;

    public MonsterSpawner(GamePanel gamePanel, Monster monster) {
        super(gamePanel);

        // Init variables
        this.monster = monster;
        this.monsters = new ArrayList<>();
        this.monsterThreads = new ArrayList<>();
    }

    // Spawns a monster at a random location
    public void spawnMonster() {
        Point spawnPos = this.randomValidSpawnPoint();          // Random x,y

        // Add new monster to array
        Monster newMonster = this.monster.newMonster(spawnPos.x, spawnPos.y);
        this.monsters.add(newMonster);
        Thread newThread = new Thread(newMonster);
        this.monsterThreads.add(newThread);
        newThread.start();

        if(this.needToSpawnCount > 0) this.needToSpawnCount--;
    }

    public void spawnMonsters(int count) {
        // We add monsters to the spawn count
        this.needToSpawnCount += count;
    }

    // Update: Updates all monsters
    public void update() {
        // If we need to spawn new monsters
        this.updateSpawn();

        for(Monster monster : this.monsters) {
            synchronized (monster) {
                monster.notifyMonster();
            }
        }

        // Delete killed monsters using streams
        this.monsters = new ArrayList<Monster>(this.monsters.stream()
                .filter(LivingEntity::getIsAlive)
                .toList());

        this.monsterThreads = new ArrayList<>(this.monsterThreads.stream()
                .filter(Thread::isAlive)
                .toList());
    }

    private void updateSpawn() {
        if(this.needToSpawnCount > 0) {
            this.tickCounter++;

            // If we need to spawn a monster
            if(this.tickCounter > this.ticksBetweenSpawns) {
                this.tickCounter = 0;

                this.spawnMonster();
            }
        }
    }

    // Draw all monsters on screen:
    public void draw(Graphics2D graphics2D) {
        for(Monster monster : this.monsters) {
            monster.draw(graphics2D);
        }
    }

    /* Getters */
    public ArrayList<Monster> getMonsters() { return this.monsters; }
    public boolean isSpawning() { return this.needToSpawnCount > 0; }
}
