package game_object;

import java.awt.Graphics;
import java.awt.Rectangle;

import user_interface.GameScreen;
import manager.EnemyManager;

public abstract class Enemy {

    public EnemyManager eManager;
    public GameScreen gameScreen;

    public Enemy(GameScreen gameScreen, EnemyManager eManager) {
        this.gameScreen = gameScreen;
        this.eManager = eManager;
    }

    public abstract void updatePosition();

    public abstract boolean spaceAvailable();

    public abstract boolean isCollision(Rectangle dinoHitBox);

    public abstract void draw(Graphics g);

    public abstract void drawHitbox(Graphics g);

    public EnemyManager geteManager() {
        return eManager;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

}
