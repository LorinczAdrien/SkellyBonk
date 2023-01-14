package entities.projectiles;

import entities.Entity;
import entities.Player;
import game_logic.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class ProjectileShooter extends Entity {
    // References
    protected GamePanel gamePanel;
    protected Player player;

    // Information about the curret status of the gun
    protected int projectileSpeed, projectileDamage;
    protected double gunAngle;
    protected int cooldown, cooldownCounter;

    // What projectile it shoots
    protected Projectile projectile;
    protected ArrayList<Projectile> projectiles;

    public ProjectileShooter(GamePanel gamePanel, Projectile projectile) {
        super(gamePanel);

        // References
        this.gamePanel = gamePanel;
        this.player = this.gamePanel.getPlayer();

        // Projectile
        this.projectile = projectile;
        this.projectiles = new ArrayList<>();
        this.spriteSize = this.gamePanel.getSpriteSize();

        // Logic
        this.collisionOn = false;
        this.cooldownCounter = 0;
    }

    public void update() {
        this.cooldownCounter++;

        // Update sprites
        this.spriteCounter++;
        if (this.spriteCounter > 10) {
            this.spriteInd = this.spriteInd == 0 ? 1 : 0;
            this.spriteCounter = 0;
        }

        // Update every projectile's position
        for(Projectile projectile : projectiles) {
            projectile.update();
        }

        // Delete hit projectiles
        int i = 0;
        while(i < this.projectiles.size()) {
            Projectile projectile = this.projectiles.get(i);

            if(projectile.isHit()) {
                this.projectiles.remove(projectile);
            } else {
                i++;
            }
        }
    }
    public void executeShot(int x, int y, int targetX, int targetY) {
        if(this.cooldownCounter > this.cooldown) {
            this.cooldownCounter = 0;

            // Creating new projectiles
            Projectile newProjectile = this.projectile.newProjectile();
            newProjectile.shootProjectile(x, y, targetX, targetY);
            this.projectiles.add(newProjectile);
        } else {
            // System.out.println("Cooling down...");
        }
    }

    public void draw(Graphics2D graphics2D) {
        this.drawGun(graphics2D);

        // Draw every projectile
        for(Projectile projectile : projectiles) {
            projectile.draw(graphics2D);
        }
    }

    protected void drawGun(Graphics2D graphics2D) {
        // Draw gun
        BufferedImage image = this.animIdle.get(this.spriteInd);
        graphics2D.drawImage(image, this.xCoord, this.yCoord, this.spriteSize / 2, this.spriteSize / 2, null);
    }

    public abstract void updateLocation(int x, int y);

    public void deleteProjectiles() { this.projectiles.clear(); }

    /* Getters */
    public ArrayList<Projectile> getProjectiles() { return this.projectiles; }
    public double getProjectileSpeed() {
        return this.projectileSpeed;
    }
    public double getGunAngle() {
        return this.gunAngle;
    }
    public boolean canShoot() { return this.cooldownCounter > this.cooldown; }

    /* Setters */
    void setProjectileSpeed(int newSpeed) {
        this.projectileSpeed = newSpeed;
    }
    void setGunAngle(double newAngle) {
        this.gunAngle = newAngle;
    }
}
