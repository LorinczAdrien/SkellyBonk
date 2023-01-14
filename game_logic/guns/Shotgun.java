package game_logic.guns;

import entities.projectiles.Projectile;
import entities.projectiles.ProjectileShooter;
import game_logic.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Shotgun extends ProjectileShooter {
    Random rand = new Random();
    int xOffset, yOffset, projectileCount, xVariation, yVariation;
    public Shotgun(GamePanel gamePanel, Projectile projectile) {
        super(gamePanel, projectile);
        this.collisionOn = true;

        // Pistol stats
        this.projectileSpeed = 15;
        this.projectileDamage = 2;      // Each projectile's base damage
        this.gunAngle = Math.toRadians(45);
        this.cooldownCounter = 0;
        this.cooldown = 45;            // Fires every 15 ticks (0.25 second)

        // Shotgun specific
        this.projectileCount = 5;
        this.xVariation = 100;
        this.yVariation = 100;

        // Set projectile stats
        this.projectile.setProjectileSpeed(this.projectileSpeed);
        this.projectile.setProjectileDamage(this.projectileDamage);

        this.loadAnimations();
        this.setupSprite();
    }

    @Override
    public void executeShot(int x, int y, int targetX, int targetY) {
        if(this.cooldownCounter > this.cooldown) {
            this.cooldownCounter = 0;

            // Create projectiles
            for(int i = 0; i < this.projectileCount; ++i) {
                // Creating new projectiles
                int randomXSpread = rand.nextInt(-this.xVariation, this.xVariation);
                int randomYSpread = rand.nextInt(-this.yVariation, this.yVariation);

                Projectile newProjectile = this.projectile.newProjectile();
                newProjectile.shootProjectile(x, y, targetX + randomXSpread, targetY + randomYSpread);
                this.projectiles.add(newProjectile);
            }
        } else {
            // System.out.println("Cooling down...");
        }
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
        this.yOffset = -20;
    }
}
