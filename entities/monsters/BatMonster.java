package entities.monsters;

import game_logic.panels.GamePanel;

import java.awt.*;
public class BatMonster extends Monster {

    public BatMonster(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y);

        // Init variables
        this.initVariables();
    }

    private void initVariables() {
        // Stats
        this.health = this.maxHealth = 2;
        this.speed = 20; this.attackDamage = 2;
        this.moveCooldown = 10;     // It moves every 0.5 seconds
        this.attackCooldown = 30;   // Can attack every 0.5 seconds

        // Collision box
        this.collisionOn = true; this.canFly = true;
        this.collisionBox = new Rectangle(this.xCoord + 5, this.yCoord + 10, this.spriteSize / 6 * 5, this.spriteSize / 6 * 5);

        this.loadAnimations();
    }

    public Monster newMonster(int x, int y) {
        return new BatMonster(this.gamePanel, x, y);
    }
}
