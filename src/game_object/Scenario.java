package game_object;

import java.awt.Graphics;

import user_interface.GameScreen;

public abstract class Scenario {

    public GameScreen gameScreen;

    public Scenario(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public abstract void updatePosition();

    public abstract void draw(Graphics g);

    public GameScreen getGameScreen() {
        return gameScreen;
    }

}
