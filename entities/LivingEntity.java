package entities;

import game_logic.panels.GamePanel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class LivingEntity extends Entity{
    // Living entities have health, damage...
    protected int health = 5, maxHealth = 5, speed = 1;
    protected boolean isAlive = true;

    // Living entities have more animations
    protected ArrayList<BufferedImage> animUp, animLeft, animDown, animRight, animDeath;

    public LivingEntity(GamePanel gamePanel) {
        super(gamePanel);
    }

    @Override
    protected void loadAnimations() {
        this.animUp = new ArrayList<>(); this.animLeft = new ArrayList<>(); this.animDown = new ArrayList<>();
        this.animRight = new ArrayList<>(); this.animIdle = new ArrayList<>(); this.animDeath = new ArrayList<>();

        // Reading in the images for different animations
        this.loadOneAnimation(this.animUp, "up");
        this.loadOneAnimation(this.animLeft, "left");
        this.loadOneAnimation(this.animDown, "down");
        this.loadOneAnimation(this.animRight, "right");
        this.loadOneAnimation(this.animIdle, "idle");
        this.loadOneAnimation(this.animDeath, "death");
    }

    /* Getters */
    public int getSpeed() {
        return this.speed;
    }
    public int getHealth() {
        return this.health;
    }
    public int getMaxHealth() { return this.maxHealth; }
    public boolean getIsAlive() { return this.isAlive; }
    public boolean isAttacking() { return this.direction.equals("attack"); }

    /* Setters */

}
