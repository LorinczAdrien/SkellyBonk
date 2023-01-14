package entities.monsters;

import entities.projectiles.EnemyProjectile;
import entities.projectiles.Projectile;
import entities.projectiles.ProjectileShooter;
import game_logic.guns.EnemyGun;
import game_logic.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DragonMonster extends Monster {
    // Look out honey, the monster has a gun
    ProjectileShooter gun;
    protected ArrayList<BufferedImage> animAttack;    // Animations for the attack

    public DragonMonster(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y);

        // Init variables
        this.initVariables();
    }

    private void initVariables() {
        // Stats
        this.health = this.maxHealth = 15;
        this.speed = 8; this.attackDamage = 4;
        this.moveCooldown = 10;     // It moves every 0.5 seconds
        this.attackCooldown = 120;   // Can attack every 2 seconds
        this.spriteCooldown = 30;

        // Gun
        this.gun = new EnemyGun(this.gamePanel, new EnemyProjectile(this.gamePanel, 10, 4));

        // Collision box
        this.collisionOn = true; this.canFly = false; this.canAttack = true;
        this.collisionBox = new Rectangle(this.xCoord + 5, this.yCoord + 10, this.spriteSize / 6 * 5, this.spriteSize / 6 * 5);

        this.loadAnimations();
        this.loadAttackAnimation();
    }

    private void loadAttackAnimation() {
        this.animAttack = new ArrayList<>();
        this.loadOneAnimation(this.animAttack, "attack");
    }

    @Override
    protected void monsterAttack() {
        this.gun.update();  // We update the gun

        if(this.gun.canShoot()) {
            int playerX = this.player.getxCoord(), playerY = this.player.getyCoord();
            this.gun.executeShot(this.xCoord + this.spriteSize / 2, this.yCoord + this.spriteSize / 2, playerX, playerY);

            // Monster is now attacking
            this.direction = "attack";
            this.spriteCounter = 0;
        }

        if(this.isAttacking() && this.spriteCounter > 10) {
            this.direction = "down";
            this.spriteCounter = 0;
        }
    }

    @Override
    protected void updatePosition() {
        if(!this.isAttacking()) {
            int dx = (int) (this.xVelocity * this.speed), dy = (int) (this.yVelocity * this.speed);
            this.moveY(dy);
            this.moveX(dx);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        // Draw shot projectiles
        this.gun.draw(graphics2D);

        BufferedImage image = null;
        switch (this.direction) {
            case "up"       -> image = this.animUp.get(this.spriteInd);
            case "left"     -> image = this.animLeft.get(this.spriteInd);
            case "down"     -> image = this.animDown.get(this.spriteInd);
            case "right"    -> image = this.animRight.get(this.spriteInd);
            case "idle"     -> image = this.animIdle.get(this.spriteInd);
            case "attack"   -> image = this.animAttack.get(this.spriteInd);
            default         -> System.out.println("Error (" + this.getClass().getSimpleName() + "): No animation was chosen!");
        }

        graphics2D.drawImage(image, this.xCoord, this.yCoord, this.spriteSize, this.spriteSize, null);
    }

    /* Getters */
    @Override
    public ArrayList<Projectile> getMonsterProjectiles() { return this.gun.getProjectiles(); }

    public Monster newMonster(int x, int y) {
        return new DragonMonster(this.gamePanel, x, y);
    }
}
