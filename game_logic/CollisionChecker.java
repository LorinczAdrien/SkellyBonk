package game_logic;

import entities.Player;
import entities.monsters.DragonMonster;
import entities.monsters.Monster;
import entities.projectiles.Projectile;
import entities.projectiles.ProjectileShooter;
import game_logic.spawners.MonsterSpawner;

import java.awt.*;
import java.util.ArrayList;

public class CollisionChecker {
    // References
    private Player player;

    private MonsterSpawner slimeSpawner, batSpawner, ghostSpawner, dragonSpawner;


    public CollisionChecker(Player player) {
        this.player = player;
    }

    public int checkPlayerCollision() {
        Rectangle playerCollisionBox = this.player.getCollisionBox();

        if(this.dragonSpawner != null) {
            ArrayList<Monster> dragons = this.dragonSpawner.getMonsters();
            int damageTaken = damageFromMonsterCategory(dragons, playerCollisionBox);
            if(damageTaken > 0) return damageTaken;
            // Check if any dragon projectiles hit the player
            for(Monster dragon : dragons) {
                ArrayList<Projectile> projectiles = dragon.getMonsterProjectiles();

                for(Projectile projectile : projectiles) {
                    if(playerCollisionBox.intersects(projectile.getCollisionBox())) {
                        return projectile.getProjectileDamage();
                    }
                }
            }
        }

        if(this.ghostSpawner != null) {
            ArrayList<Monster> ghosts = this.ghostSpawner.getMonsters();
            int damageTaken = damageFromMonsterCategory(ghosts, playerCollisionBox);
            if(damageTaken > 0) return damageTaken;
        }

        if(this.batSpawner != null) {
            ArrayList<Monster> bats = this.batSpawner.getMonsters();
            int damageTaken = damageFromMonsterCategory(bats, playerCollisionBox);
            if(damageTaken > 0) return damageTaken;
        }

        if(this.slimeSpawner != null) {
            ArrayList<Monster> slimes = this.slimeSpawner.getMonsters();
            int damageTaken = damageFromMonsterCategory(slimes, playerCollisionBox);
            if(damageTaken > 0) return damageTaken;
        }

        return 0;
    }

    private int damageFromMonsterCategory(ArrayList<Monster> monsters, Rectangle playerCollisionBox) {
        for(Monster monster : monsters) {
            Rectangle monsterBox = monster.getCollisionBox();
            if(playerCollisionBox.intersects(monsterBox)) return monster.getAttackDamage();
        }
        return 0;
    }

    public int checkMonsterCollision(Monster monster) {
        // Monster's collision box
        Rectangle monsterCollisionBox = monster.getCollisionBox();

        // The gun and projectiles the player currently uses
        ProjectileShooter gun = this.player.getGun();
        ArrayList<Projectile> projectiles = gun.getProjectiles();

        // Using streams, detect collisions
        boolean colideWithPlayerProjectiles = projectiles.stream().anyMatch(projectile -> monsterCollisionBox.intersects(projectile.getCollisionBox()));
        if(colideWithPlayerProjectiles) {
            return projectiles.get(0).getProjectileDamage();
        }

//        for(Projectile projectile : projectiles) {
//            Rectangle projectileBox = projectile.getCollisionBox();
//            if(monsterCollisionBox.intersects(projectileBox)) return projectile.getProjectileDamage();
//        }
        return 0;
    }

    /* Setters */
    public void setSlimeSpawner(MonsterSpawner spawner) {
        this.slimeSpawner = spawner;
    }
    public void setBatSpawner(MonsterSpawner spawner) {
        this.batSpawner = spawner;
    }
    public void setGhostSpawner(MonsterSpawner spawner) {
        this.ghostSpawner = spawner;
    }
    public void setDragonSpawner(MonsterSpawner spawner) {
        this.dragonSpawner = spawner;
    }
}
