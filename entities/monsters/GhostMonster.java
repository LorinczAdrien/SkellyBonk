package entities.monsters;

import game_logic.panels.GamePanel;

import java.awt.*;
public class GhostMonster extends Monster {

    public GhostMonster(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y);

        // Init variables
        this.initVariables();
    }

    private void initVariables() {
        // Stats
        this.health = this.maxHealth = 10;
        this.speed = 15; this.attackDamage = 3;
        this.moveCooldown = 10;      // It moves every 0.5 seconds
        this.attackCooldown = 30;   // Can attack every 0.5 seconds
        this.spriteCooldown = 20;

        // Collision box
        this.collisionOn = true; this.canFly = true;
        this.collisionBox = new Rectangle(this.xCoord + 5, this.yCoord + 10, this.spriteSize / 6 * 5, this.spriteSize / 6 * 5);

        this.loadAnimations();
    }

    @Override
    protected void updatePosition() {
        // Ghosts have a special ability -> If they are decently close to the player, they will gain a huge speed increase
        double arenaAverageDistance = (this.gamePanel.getPanelWidth() + this.gamePanel.getPanelHeight()) / 2.0;
        int my_x = this.xCoord, my_y = this.yCoord, player_x = this.player.getxCoord(), player_y = this.player.getyCoord();
        double distanceFromPlayer = Math.sqrt((my_x - player_x) * (my_x - player_x) + (my_y - player_y) * (my_y - player_y));

        int dx = (int) (this.xVelocity * this.speed), dy = (int) (this.yVelocity * this.speed);
        if(distanceFromPlayer < arenaAverageDistance / 4) {
            dx *= 2.5; dy *= 2.5;
        }

        this.moveY(dy);
        this.moveX(dx);
    }

    public Monster newMonster(int x, int y) {
        return new GhostMonster(this.gamePanel, x, y);
    }
}
