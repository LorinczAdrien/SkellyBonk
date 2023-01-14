package game_logic.guns;

import entities.projectiles.Projectile;
import entities.projectiles.ProjectileShooter;
import game_logic.panels.GamePanel;

public class Pistol extends ProjectileShooter {
    int xOffset, yOffset;
    public Pistol(GamePanel gamePanel, Projectile projectile) {
        super(gamePanel, projectile);
        this.collisionOn = true;

        // Pistol stats
        this.projectileSpeed = 15;
        this.projectileDamage = 1;
        this.gunAngle = Math.toRadians(45);
        this.cooldownCounter = 0;
        this.cooldown = 15;            // Fires every 15 ticks (0.25 second)

        // Set projectile stats
        this.projectile.setProjectileSpeed(this.projectileSpeed);
        this.projectile.setProjectileDamage(this.projectileDamage);

        this.loadAnimations();
        this.setupSprite();
    }

    @Override
    public void updateLocation(int x, int y) {
        this.xCoord = x + this.xOffset;
        this.yCoord = y + this.yOffset;
    }

    private void setupSprite() {
        this.xOffset = 10;
        this.yOffset = -10;
    }
}
