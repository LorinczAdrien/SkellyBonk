package game_logic.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private boolean upPressed = false, leftPressed = false, downPressed = false, rightPressed = false;

    private boolean switchToPistol = false, switchToRifle = false, switchToShotgun = false;

    @Override
    public void keyTyped(KeyEvent event) {
        // Ignore (We don't need this one)
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        // Movement
        if(keyCode == KeyEvent.VK_W) upPressed = true;
        if(keyCode == KeyEvent.VK_A) leftPressed = true;
        if(keyCode == KeyEvent.VK_S) downPressed = true;
        if(keyCode == KeyEvent.VK_D) rightPressed = true;

        // Gun switching
        if(keyCode == KeyEvent.VK_1) switchToPistol = true;
        if(keyCode == KeyEvent.VK_2) switchToRifle = true;
        if(keyCode == KeyEvent.VK_3) switchToShotgun = true;

//        if(keyCode == KeyEvent.VK_E) spawnSlime = true;
//        if(keyCode == KeyEvent.VK_R) spawnBat = true;
//        if(keyCode == KeyEvent.VK_F) spawnGhost = true;
//        if(keyCode == KeyEvent.VK_G) spawnDragon = true;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        int keyCode = event.getKeyCode();

        // Movement
        if(keyCode == KeyEvent.VK_W) upPressed = false;
        if(keyCode == KeyEvent.VK_A) leftPressed = false;
        if(keyCode == KeyEvent.VK_S) downPressed = false;
        if(keyCode == KeyEvent.VK_D) rightPressed = false;

        // Gun switching
        if(keyCode == KeyEvent.VK_1) switchToPistol = false;
        if(keyCode == KeyEvent.VK_2) switchToRifle = false;
        if(keyCode == KeyEvent.VK_3) switchToShotgun = false;

//        if(keyCode == KeyEvent.VK_E) spawnSlime = false;
//        if(keyCode == KeyEvent.VK_R) spawnBat = false;
//        if(keyCode == KeyEvent.VK_F) spawnGhost = false;
//        if(keyCode == KeyEvent.VK_G) spawnDragon = false;
    }

    /* Getters */
    public boolean isUpPressed() { return this.upPressed; }
    public boolean isLeftPressed() { return this.leftPressed; }
    public boolean isDownPressed() { return this.downPressed; }
    public boolean isRightPressed() { return this.rightPressed; }
    public boolean isMoving() { return this.upPressed || this.leftPressed || this.downPressed || this.rightPressed; }
    public boolean isSwitchToPistol() { return this.switchToPistol; }
    public boolean isSwitchToRifle() { return this.switchToRifle; }
    public boolean isSwitchToShotgun() { return this.switchToShotgun; }
}
