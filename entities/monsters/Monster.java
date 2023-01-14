package entities.monsters;

import entities.LivingEntity;
import entities.Player;
import entities.projectiles.Projectile;
import game_logic.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Monster extends LivingEntity implements Runnable {
    // Every monster has a reference to the player and gamepanel (for collisions)
    protected final Player player;
    protected final GamePanel gamePanel;

    // Damage and stuff
    protected int attackDamage, attackCooldown = 60, attackCooldownCounter = 0, hitCooldown, hitCooldownCounter = 0;
    protected boolean canAttack = false;
    private boolean tickPassed = false;

    // Other stats
    protected int moveCooldown, moveCooldownCounter = 0;
    protected double xVelocity, yVelocity;

    public Monster(GamePanel gamePanel, int x, int y) {
        super(gamePanel);

        this.gamePanel = gamePanel;
        this.player = this.gamePanel.getPlayer();

        // Init variables
        this.xCoord = x; this.yCoord = y; this.spriteSize = this.gamePanel.getSpriteSize();
        this.xVelocity = 0; this.yVelocity = 0; this.attackDamage = 1;

        // Basic stats
        this.hitCooldown = 15;
    }

    @Override
    public void run() {
        while(this.isAlive) {
            synchronized (this) {
                while(!this.tickPassed) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println("Wait error");
                    }
                }

                // A tick has passed so we update
                this.update();
                this.tickPassed = false;
            }
        }
    }

    public void notifyMonster() {
        synchronized (this) {
            this.tickPassed = true;
            notify();
        }
    }

    // Every monster has an update and draw method
    public void update() {
        // See if monster took damage
        this.updateMonsterHealth();

        this.moveCooldownCounter++;
        if(this.moveCooldownCounter > this.moveCooldown) {
            this.moveCooldownCounter = 0;

            // We update our direction
            this.updateTargetDirection();
            this.updatePosition();
        }

        // Attack if it can attack
        if(this.canAttack) this.monsterAttack();

        // Update sprites
        this.spriteCounter++;
        if (this.spriteCounter > this.spriteCooldown) {
            this.spriteInd = this.spriteInd == 0 ? 1 : 0;
            this.spriteCounter = 0;
        }
    }

    protected void monsterAttack() {
        // If it attacks override
    }

    protected void updatePosition() {
        int dx = (int) (this.xVelocity * this.speed), dy = (int) (this.yVelocity * this.speed);
        this.moveY(dy);
        this.moveX(dx);
    }

    // Updates the direction vector, so it points to the player's direction
    protected void updateTargetDirection() {
        // Calculating the direction
        double totalAllowedMovement = 1.0;
        double xDistanceFromTarget = Math.abs(this.xCoord - this.player.getxCoord()), yDistanceFromTarget = Math.abs(this.yCoord - this.player.getyCoord());
        double totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
        double xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
        this.xVelocity = xPercentOfMovement; this.yVelocity = totalAllowedMovement - xPercentOfMovement;
        if(this.player.getxCoord() < this.xCoord) this.xVelocity *= -1.0;
        if(this.player.getyCoord() < this.yCoord) this.yVelocity *= -1.0;
    }

    protected void updateMonsterHealth() {
        this.hitCooldownCounter++;
        if(this.hitCooldownCounter > this.hitCooldown) {
            int damageTaken = this.gamePanel.getCollisionChecker().checkMonsterCollision(this);
            if (damageTaken > 0) {
                this.health -= damageTaken; this.hitCooldownCounter = 0;
                if (this.health <= 0) this.monsterDies();
            }
        }
    }

    protected void monsterDies() {
        this.isAlive = false;

        // Increase player score and xp
        this.gamePanel.getPlayer().increaseXP(this.maxHealth);
        this.gamePanel.getPlayer().increaseScore(this.maxHealth * 10);
    }

    public void draw(Graphics2D graphics2D) {
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

//        graphics2D.setColor(Color.RED);
//        graphics2D.drawRect(this.collisionBox.x, this.collisionBox.y, this.collisionBox.width, this.collisionBox.height);
    }

    public abstract Monster newMonster(int x, int y);

    /* Getters */
    public int getAttackDamage() { return this.attackDamage; }
    public ArrayList<Projectile> getMonsterProjectiles() { return null; }
}
