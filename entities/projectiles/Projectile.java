package entities.projectiles;

import entities.Entity;
import game_logic.panels.GamePanel;

import java.awt.*;

public abstract class Projectile extends Entity {
    // References
    protected GamePanel gamePanel;

    protected boolean shot = false, hit = false;

    // Stats of the projectile
    protected int projectileSize = 15, projectileDamage, projectileSpeed;
    protected double xVelocity, yVelocity;

    public Projectile(GamePanel gamePanel, int projectileSpeed, int projectileDamage) {
        super(gamePanel);

        // Game panel
        this.gamePanel = gamePanel;
        this.projectileSpeed = projectileSpeed;
        this.projectileDamage = projectileDamage;

        // Initialize variables
        this.initVariables();
    }

    private void initVariables() {
        // Variables
        this.xVelocity = 0;
        this.yVelocity = 0;

        // Collision box
        this.collisionOn = true;
        this.collisionBox = new Rectangle(0, 0, this.projectileSize, this.projectileSize);
    }

    public void shootProjectile(int x, int y, int targetX, int targetY) {
        if(!this.shot) {
            this.shot = true;

            // Entity details
            this.xCoord = x; this.yCoord = y;
            this.collisionBox.x += this.xCoord; this.collisionBox.y += this.yCoord;

            // Calculating the direction
            double totalAllowedMovement = 1.0;
            double xDistanceFromTarget = Math.abs(x - targetX), yDistanceFromTarget = Math.abs(y - targetY);
            double totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
            double xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
            this.xVelocity = xPercentOfMovement; this.yVelocity = totalAllowedMovement - xPercentOfMovement;
            if(targetX < x) this.xVelocity *= -1.0;
            if(targetY < y) this.yVelocity *= -1.0;
        }
    }

    public void update() {
        int dx = (int) (this.xVelocity * this.projectileSpeed), dy = (int) (this.yVelocity * this.projectileSpeed);
        this.xCoord += dx; this.collisionBox.x += dx;
        this.yCoord += dy; this.collisionBox.y += dy;
        if(!isValidPosition(this.collisionBox)) {   // If we hit a wall the bullet dies
            this.hit = true;
        }
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(220, 202, 9));
        graphics2D.fillOval(this.xCoord, this.yCoord, this.projectileSize, this.projectileSize);

//        graphics2D.setColor(Color.red);
//        graphics2D.fillRect(this.collisionBox.x, this.collisionBox.y, this.collisionBox.width, this.collisionBox.height);
    }

    /* Getters */
    public boolean isHit() {
        return this.hit;
    }
    public int getProjectileDamage() { return this.projectileDamage; }

    public abstract Projectile newProjectile();

    /* Setters */
    public void setProjectileSpeed(int newSpeed) { this.projectileSpeed = newSpeed; }
    public void setProjectileDamage(int newDamage) { this.projectileDamage = newDamage; }
}
