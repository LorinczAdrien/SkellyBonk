package entities;

import entities.projectiles.BasicProjectile;
import game_logic.guns.Pistol;
import game_logic.guns.Shotgun;
import game_logic.guns.SniperRifle;
import game_logic.input.MouseHandler;
import entities.projectiles.ProjectileShooter;
import game_logic.spawners.Spawner;
import game_logic.panels.GamePanel;
import game_logic.input.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends LivingEntity {
    // References to Game logic
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;

    // Logic
    private ProjectileShooter gun, pistol, sniperRifle, shotgun;
    private int xp = 0, score = 0, hitCooldown, hitCooldownCounter;

    public Player(GamePanel gamePanel) {
        super(gamePanel);

        // References
        this.gamePanel = gamePanel;
        this.keyHandler = this.gamePanel.getKeyHandler();
        this.mouseHandler = this.gamePanel.getMouseHandler();
        this.gun = gun;

        // Other
        this.spriteSize = this.gamePanel.getSpriteSize();

        this.initVariables();
    }

    private void initVariables() {
        // Player stats
        this.speed = 5; this.health = this.maxHealth = 20; this.xp = 0;
        this.hitCooldownCounter = 0; this.hitCooldown = 30;     // Player can be atacked every 0.5 seconds

        // Player spawn
        Point randomSpawn = new Spawner(this.gamePanel).randomValidSpawnPoint();
        this.xCoord = randomSpawn.x; this.yCoord = randomSpawn.y;

        // Default gun -> Pistol
        this.pistol = new Pistol(this.gamePanel, new BasicProjectile(this.gamePanel, 0, 0));
        this.sniperRifle = new SniperRifle(this.gamePanel, new BasicProjectile(this.gamePanel, 0, 0));
        this.shotgun = new Shotgun(this.gamePanel, new BasicProjectile(this.gamePanel, 0, 0));
        this.gun = this.pistol;
        this.gun.updateLocation(this.xCoord, this.yCoord);

        // Collision box (Player has collision)
        int collisionSize = this.spriteSize - this.gamePanel.getOriginalSpriteSize();
        this.collisionBox = new Rectangle(this.xCoord + this.spriteSize / 8, this.yCoord + this.spriteSize / 6, collisionSize, collisionSize);   // A smaller collision box then the sprite size
        this.collisionOn = true;

        this.loadAnimations();
    }

    public void update() {
        // Update gun
        this.gun.update();

        // Update health if we were attacked
        this.updatePlayerHealth();
        this.updatePlayerPosition();

        // Update sprites
        this.spriteCounter++;
        if (this.spriteCounter > 10) {
            this.spriteInd = this.spriteInd == 0 ? 1 : 0;
            this.spriteCounter = 0;
        }

        this.updateGuns();  // See if we need to switch guns
        this.shootGun();
    }

    private void updatePlayerHealth() {
        this.hitCooldownCounter++;

        if(this.hitCooldownCounter >= this.hitCooldown) {
            // Check if we can be harmed
            int damageTaken = this.gamePanel.getCollisionChecker().checkPlayerCollision();
            if (damageTaken > 0) {
                System.out.println("Debug: Damage taken: " + damageTaken);
                this.health -= damageTaken;
                // Decrease player score
                this.decreaseScore(damageTaken * 50);
                this.gamePanel.playerDamaged();         // Play bonk sound effect

                if(this.health <= 0) {                   // If it's oever
                    this.gamePanel.setGameOver(true);
                }

                this.hitCooldownCounter = 0;
            }
        }
    }

    private void updatePlayerPosition() {
        // Movement
        if(keyHandler.isMoving()) {
            // WASD key movement
            if (keyHandler.isUpPressed()) {
                this.moveUp(this.speed);
                this.direction = "up";
            }
            if (keyHandler.isLeftPressed()) {
                this.moveLeft(this.speed);
                this.direction = "left";
            }
            if (keyHandler.isDownPressed()) {
                this.moveDown(this.speed);
                this.direction = "down";
            }
            if (keyHandler.isRightPressed()) {
                this.moveRight(this.speed);
                this.direction = "right";
            }
            this.gun.updateLocation(this.xCoord, this.yCoord);
        } else {
            this.direction = "idle";
        }
    }

    private void updateGuns() {
        if(this.keyHandler.isSwitchToPistol()) {
            if(this.gun != this.pistol) this.setGun(this.pistol);
        } else if(this.keyHandler.isSwitchToRifle()) {
            if(this.gun != this.sniperRifle) this.setGun(this.sniperRifle);
        } else if(this.keyHandler.isSwitchToShotgun()) {
            if(this.gun != this.shotgun) this.setGun(this.shotgun);
        }
    }

    private void shootGun() {
        // Attack
        if(this.mouseHandler.isLeftMousePressed()) {
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mousePos, this.gamePanel);    // Convert global coordinates to relative (Frame)

            if(this.gun.canShoot()) this.gamePanel.shootGun();
            this.gun.executeShot(this.xCoord + this.spriteSize / 3, this.yCoord, mousePos.x, mousePos.y);
            this.mouseHandler.setLeftMousePressed(false);
        }
    }

    public void draw(Graphics2D graphics2D) {
        // Draw projectiles
        this.gun.draw(graphics2D);

        BufferedImage image = null;
        switch (this.direction) {
            case "up"       -> image = this.animUp.get(this.spriteInd);
            case "left"     -> image = this.animLeft.get(this.spriteInd);
            case "down"     -> image = this.animDown.get(this.spriteInd);
            case "right"    -> image = this.animRight.get(this.spriteInd);
            case "idle"     -> image = this.animIdle.get(this.spriteInd);
            default         -> System.out.println("Error (" + this.getClass().getSimpleName() + "): No animation was chosen!");
        }

        graphics2D.drawImage(image, this.xCoord, this.yCoord, this.spriteSize, this.spriteSize, null);

        // Debug: Draw collision box
//        this.gamePanel.drawRectangle(this.collisionBox, graphics2D);
    }

    public void increaseXP(int amount) { this.xp += amount; }
    public void increaseScore(int amount) { this.score += amount; }
    public void decreaseScore(int amount) { this.score -= amount; }

    /* Getters */
    public int getXp() {
        return this.xp;
    }
    public int getScore() { return this.score; }
    public ProjectileShooter getGun() { return this.gun; }

    /* Setters */
    public void setGun(ProjectileShooter gun) {
        this.gun.deleteProjectiles();
        this.gun = gun;
        this.gun.updateLocation(this.xCoord, this.yCoord);
    }
}
