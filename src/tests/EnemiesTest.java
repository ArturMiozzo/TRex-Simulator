package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import game_object.Cactuses;
import game_object.Birds;
import game_object.Rocks;
import user_interface.GameScreen;
import gameplay.EnemyManager;

public class EnemiesTest {

    private GameScreen gameScreen;
    private EnemyManager enemyManager;
    private Cactuses cactuses;
    private Birds birds;
    private Rocks rocks;

    @Before
    public void setUp() {
        gameScreen = new GameScreen();
        enemyManager = new EnemyManager(gameScreen);
        cactuses = new Cactuses(gameScreen, enemyManager);
        birds = new Birds(gameScreen, enemyManager);
        rocks = new Rocks(gameScreen, enemyManager);
    }

    @Test
    public void testSpaceAvailable() {
        cactuses.createCactuses();
        birds.createBird();
        rocks.createRocks();

        assertTrue(cactuses.spaceAvailable());
        assertTrue(birds.spaceAvailable());
        assertTrue(rocks.spaceAvailable());
    }

    @Test
    public void testClearEnemies() {
        cactuses.createCactuses();
        birds.createBird();
        rocks.createRocks();

        cactuses.clearCactuses();
        birds.clearBirds();
        rocks.clearRocks();

        // Check if the enemy lists are empty
        assertTrue(cactuses.getCactusesList().isEmpty());
        assertTrue(birds.getBirdsList().isEmpty());
        assertTrue(rocks.getRocksList().isEmpty());
    }
}
