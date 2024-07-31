package game_object;

import user_interface.GameScreen;
import util.Resource;

import static user_interface.GameScreen.GROUND_Y;
import static user_interface.GameWindow.SCREEN_WIDTH;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gameplay.EnemyManager;

public class Rocks extends Enemy {

    private class Rock {
        private BufferedImage rockImage;
        private double x;
        private int y;
        private double yVelocity;

        private Rock(BufferedImage rockImage, double x, int y) {
            this.rockImage = rockImage;
            this.x = x;
            this.y = y;
            this.yVelocity = 0;
        }
    }

    private static final double GRAVITY = 0.3;
    private static final double JUMP_STRENGTH = -9;
    private static final double HITBOX_X = 2.7;
    private static final int HITBOX_Y = 25;
    private static final int MAX_ROCK_GROUP = 1;

    private List<Rock> rocks;

    public Rocks(GameScreen gameScreen, EnemyManager enemyManager) {
        super(gameScreen, enemyManager);
        rocks = new ArrayList<Rock>();
    }

    public List<Rock> getRocksList() {
        return rocks;
    }

    @Override
    public void updatePosition() {
        for (Iterator<Rock> i = rocks.iterator(); i.hasNext();) {
            Rock rock = i.next();
            rock.x += (gameScreen.getSpeedX() + gameScreen.getSpeedX() / 5);

            rock.yVelocity += GRAVITY;
            rock.y += rock.yVelocity;

            if (rock.y >= GROUND_Y - rock.rockImage.getHeight()) {
                rock.y = GROUND_Y - rock.rockImage.getHeight();
                rock.yVelocity = JUMP_STRENGTH; // Pula novamente ao atingir o chão
            }

            if ((int) rock.x + rock.rockImage.getWidth() < 0) {
                i.remove();
            }
        }
    }

    @Override
    public boolean spaceAvailable() {
        for (Rock rock : rocks) {
            if (SCREEN_WIDTH - (rock.x + rock.rockImage.getWidth()) < enemyManager.getDistanceBetweenEnemies()) {
                return false;
            }
        }
        return true;
    }

    public boolean createRocks() {
        if (Math.random() * 100 < enemyManager.getRocksPercentage()) {
            for (int i = 0, numberOfRocks = (int) (Math.random() * MAX_ROCK_GROUP + 1); i < numberOfRocks; i++) {
                BufferedImage rockImage = Resource.ROCK_SPRITE;
                int x = SCREEN_WIDTH;
                int y = GROUND_Y - rockImage.getHeight();

                if (i > 0) {
                    x = (int) rocks.get(rocks.size() - 1).x + rocks.get(rocks.size() - 1).rockImage.getWidth();
                }

                rocks.add(new Rock(rockImage, x, y));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isCollision(Rectangle dinoHitBox) {
        for (Rock rock : rocks) {
            Rectangle rockHitBox = getHitbox(rock);
            if (rockHitBox.intersects(dinoHitBox)) {
                return true;
            }
        }
        return false;
    }

    private Rectangle getHitbox(Rock rock) {
        return new Rectangle(
                (int) rock.x + (int) (rock.rockImage.getWidth() / HITBOX_X),
                rock.y + rock.rockImage.getHeight() / HITBOX_Y,
                rock.rockImage.getWidth() - (int) (rock.rockImage.getWidth() / HITBOX_X) * 2,
                rock.rockImage.getHeight() - rock.rockImage.getHeight() / HITBOX_Y);
    }

    public void clearRocks() {
        rocks.clear();
    }

    @Override
    public void draw(Graphics g) {
        for (Rock rock : rocks) {
            g.drawImage(rock.rockImage, (int) (rock.x), rock.y, null);
        }
    }
}
