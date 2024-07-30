package game_object;

import java.awt.Graphics;
import java.awt.Rectangle;

import user_interface.GameScreen;
import manager.EnemyManager;

public abstract class Enemy {

    public EnemyManager enemyManager;
    public GameScreen gameScreen;

    // classe base para os inimigos
    public Enemy(GameScreen gameScreen, EnemyManager enemyManager) {
        this.gameScreen = gameScreen;
        this.enemyManager = enemyManager;
    }

    public abstract void updatePosition();

    public abstract boolean spaceAvailable();

    public abstract boolean isCollision(Rectangle dinoHitBox);

    public abstract void draw(Graphics g);

    public EnemyManager getenemyManager() {
        return enemyManager;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

}
