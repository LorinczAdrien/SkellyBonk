package entities.projectiles;

import game_logic.panels.GamePanel;

public class BasicProjectile extends Projectile {
    public BasicProjectile(GamePanel gamePanel, int projectileSpeed, int projectileDamage) {
        super(gamePanel, projectileSpeed, projectileDamage);
    }

    @Override
    public Projectile newProjectile() {
        return new BasicProjectile(this.gamePanel, this.projectileSpeed, this.projectileDamage);
    }
}
