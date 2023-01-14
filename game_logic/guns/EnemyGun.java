package game_logic.guns;

import entities.projectiles.Projectile;
import entities.projectiles.ProjectileShooter;
import game_logic.panels.GamePanel;

import java.awt.*;

public class EnemyGun extends ProjectileShooter {
    public EnemyGun(GamePanel gamePanel, Projectile projectile) {
        super(gamePanel, projectile);

        // A gun has no collision
        this.collisionOn = false;

        // Pistol stats
        this.projectileSpeed = 10;
        this.projectileDamage = 4;
        this.gunAngle = Math.toRadians(45);
        this.cooldownCounter = 0;
        this.cooldown = 90;            // Fires every 15 ticks (0.25 second)

        // Set projectile stats
        this.projectile.setProjectileSpeed(this.projectileSpeed);
        this.projectile.setProjectileDamage(this.projectileDamage);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        // Draw every projectile
        for(Projectile projectile : projectiles) {
            projectile.draw(graphics2D);
        }
    }

    @Override
    public void updateLocation(int x, int y) {

    }
}
