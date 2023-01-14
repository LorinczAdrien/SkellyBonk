package game_logic.spawners;

import game_logic.panels.GamePanel;

import java.awt.*;
import java.util.Random;

public class Spawner {
    // References
    private final GamePanel gamePanel;
    private final Rectangle boundingBox, barrierMiddle;
    private final Random random = new Random();

    public Spawner(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.boundingBox = this.gamePanel.getSpawnBoundingBox();
        this.barrierMiddle = this.gamePanel.getBarrierMiddle();
    }

    public Point randomValidSpawnPoint() {
        int spriteSize = this.gamePanel.getSpriteSize();
        int start_x = this.boundingBox.x, start_y = this.boundingBox.y;
        int end_x = start_x + this.boundingBox.width, end_y = start_y + this.boundingBox.height;

        int x = random.nextInt(start_x, end_x + 1), y = random.nextInt(start_y, end_y + 1);
        Rectangle entityBox = new Rectangle(x, y, spriteSize, spriteSize);
        while(entityBox.intersects(this.barrierMiddle)) {
            x = random.nextInt(start_x, end_x + 1); y = random.nextInt(start_y, end_y + 1);
            entityBox.x = x; entityBox.y = y;
        }

        return new Point(x, y);
    }
}
