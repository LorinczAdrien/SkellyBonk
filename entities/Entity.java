package entities;

import game_logic.panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Entity {
    // Every entity contains these fields
    protected int xCoord = 0, yCoord = 0;

    // Collision
    protected final Rectangle barrierUp, barrierLeft, barrierDown, barrierRight, barrierMiddle;
    protected Rectangle collisionBox;
    protected boolean collisionOn = false, canFly = false;

    // Sprites and animations
    protected ArrayList<BufferedImage> animIdle;    // Animations for the sprite
    protected int spriteCounter = 0, spriteInd = 0, spriteSize, spriteCooldown = 10;
    protected String direction = "idle";            // In which direction it's moving

    public Entity(GamePanel gamePanel) {
        // Game panel barriers
        this.barrierUp = gamePanel.getBarrierUp();
        this.barrierRight = gamePanel.getBarrierRight();
        this.barrierDown = gamePanel.getBarrierDown();
        this.barrierLeft = gamePanel.getBarrierLeft();
        this.barrierMiddle = gamePanel.getBarrierMiddle();
    }

    // Methods to manipulate each entity
    public void moveUp(int amount) {
        if(this.collisionOn) {
        // Calculate where our step would be
        Rectangle newCollisionBox = new Rectangle(this.collisionBox); newCollisionBox.y -= amount;

        // Only move if it's a valid position
        if(this.canFly || isValidPosition(newCollisionBox)) {
            this.yCoord -= amount; this.collisionBox.y -= amount;
            }
        } else {
            this.yCoord -= amount;
        }
    }
    public void moveLeft(int amount) {
        if(this.collisionOn) {
            // Calculate where our step would be
            Rectangle newCollisionBox = new Rectangle(this.collisionBox); newCollisionBox.x -= amount;

            // Only move if it's a valid position
            if (this.canFly || isValidPosition(newCollisionBox)) {
                this.xCoord -= amount; this.collisionBox.x -= amount;
            }
        } else {
            this.xCoord -= amount;
        }
    }
    public void moveDown(int amount) {
        if(this.collisionOn) {
            // Calculate where our step would be
            Rectangle newCollisionBox = new Rectangle(this.collisionBox); newCollisionBox.y += amount;

            // Only move if it's a valid position
            if (this.canFly || isValidPosition(newCollisionBox)) {
                this.yCoord += amount; this.collisionBox.y += amount;
            }
        } else {
            this.yCoord += amount;
        }
    }
    public void moveRight(int amount) {
        if(this.collisionOn) {
            // Calculate where our step would be
            Rectangle newCollisionBox = new Rectangle(this.collisionBox); newCollisionBox.x += amount;

            // Only move if it's a valid position
            if (this.canFly || isValidPosition(newCollisionBox)) {
                this.xCoord += amount; this.collisionBox.x += amount;
            }
        } else {
            this.xCoord += amount;
        }
    }

    public void moveX(int amount) {
        if(this.collisionOn) {
            // Calculate where our step would be
            Rectangle newCollisionBox = new Rectangle(this.collisionBox);
            newCollisionBox.x += amount;

            // Only move if it's a valid position
            if (this.canFly || isValidPosition(newCollisionBox)) {
                this.xCoord += amount; this.collisionBox.x += amount;
            }
        } else {
            this.xCoord += amount;
        }

        this.direction = amount > 0 ? "right" : "left";
    }
    public void moveY(int amount) {
        if(this.collisionOn) {
            // Calculate where our step would be
            Rectangle newCollisionBox = new Rectangle(this.collisionBox);
            newCollisionBox.y += amount;

            // Only move if it's a valid position
            if (this.canFly || isValidPosition(newCollisionBox)) {
                this.yCoord += amount; this.collisionBox.y += amount;
            }
        } else {
            this.yCoord += amount;
        }

        this.direction = amount > 0 ? "down" : "up";
    }

    public boolean isValidPosition(Rectangle collisionBox) {
        return (!collisionBox.intersects(this.barrierUp) && !collisionBox.intersects(this.barrierLeft) &&
                !collisionBox.intersects(this.barrierDown) && !collisionBox.intersects(this.barrierRight) &&
                !collisionBox.intersects(this.barrierMiddle));
    }

    protected void loadAnimations() {
        this.animIdle = new ArrayList<>();

        this.loadOneAnimation(this.animIdle, "idle");
    }

    protected void loadOneAnimation(ArrayList<BufferedImage> images, String type) {
        String className = getClass().getSimpleName().toLowerCase();    // We use the class name and type to find the corresponding directory
        try {
            File path = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "images" + File.separator + className + File.separator + type);
            if(path.exists() && path.isDirectory()) {
                File[] allFiles = path.listFiles();
                if(allFiles != null) {
                    for (File allFile : allFiles) images.add(ImageIO.read(allFile));
                } else System.out.println("Error (" + className + "): " + path + " directory is null!");
            }
            else System.out.println("Error (" + className + "): " + path + " doesn't exits or isn't directory");
        } catch (IOException exception) {
            System.out.println("Error: Couldn't read " + type + " sprites");
        }
    }

    /* Getters */
    public int getxCoord() {
        return xCoord;
    }
    public int getyCoord() {
        return yCoord;
    }
    public boolean getCollisionOn() {
        return this.collisionOn;
    }
    public Rectangle getCollisionBox() { return this.collisionBox; }
}
