package game_logic.guns;

import entities.projectiles.Projectile;
import entities.projectiles.ProjectileShooter;
import game_logic.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SniperRifle extends ProjectileShooter {
    int xOffset, yOffset;
    public SniperRifle(GamePanel gamePanel, Projectile projectile) {
        super(gamePanel, projectile);
        this.collisionOn = true;

        // Pistol stats
        this.projectileSpeed = 30;
        this.projectileDamage = 5;
        this.gunAngle = Math.toRadians(45);
        this.cooldownCounter = 0;
        this.cooldown = 60;            // Fires every 15 ticks (0.25 second)

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

    @Override
    protected void drawGun(Graphics2D graphics2D) {
        // Draw gun
        BufferedImage image = this.animIdle.get(this.spriteInd);
        graphics2D.drawImage(image, this.xCoord, this.yCoord, (int) (2 * this.spriteSize / 1.5), (int) (this.spriteSize / 1.5), null);
    }

    private void setupSprite() {
        this.xOffset = -10;
        this.yOffset = -25;
    }
}
