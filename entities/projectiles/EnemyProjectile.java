package entities.projectiles;

import game_logic.panels.GamePanel;

import java.awt.*;

public class EnemyProjectile extends Projectile {
    public EnemyProjectile(GamePanel gamePanel, int projectileSpeed, int projectileDamage) {
        super(gamePanel, projectileSpeed, projectileDamage);
    }

    @Override
    public Projectile newProjectile() {
        return new EnemyProjectile(this.gamePanel, this.projectileSpeed, this.projectileDamage);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(208, 0, 82));
        graphics2D.fillOval(this.xCoord, this.yCoord, this.projectileSize, this.projectileSize);
    }
}
