package tests;

import org.junit.*;
import static org.junit.Assert.*;

import util.*;

public class ResourcesTest {

    @Test
    public void testImages() {
        assertNotNull(Resource.BIRD_SPRITE);
        assertNotNull(Resource.CACTUS_SPRITE);
        assertNotNull(Resource.ROCK_SPRITE);
        assertNotNull(Resource.DINO_RUN_SPRITE);
        assertNotNull(Resource.DINO_RUN_DOWN_SPRITE);
        assertNotNull(Resource.DINO_JUMP_SPRITE);
        assertNotNull(Resource.DINO_DEAD_SPRITE);
        assertNotNull(Resource.LAND_SPRITE);
        assertNotNull(Resource.CLOUD_SPRITE);
        assertNotNull(Resource.HI_SPRITE);
        assertNotNull(Resource.NUMBERS_SPRITE);
        assertNotNull(Resource.INTRO_SPRITE);
        assertNotNull(Resource.GAME_OVER_SPRITE);
        assertNotNull(Resource.REPLAY_SPRITE);
    }

    @Test
    public void testGetImage() {
        assertNotNull(Resource.getImage("resources/rock.png"));
        assertNotNull(Resource.getImage("resources/dino-jump.png"));
        assertNotNull(Resource.getImage("resources/dino-dead.png"));
        assertNotNull(Resource.getImage("resources/land.png"));
        assertNotNull(Resource.getImage("resources/cloud.png"));
        assertNotNull(Resource.getImage("resources/hi.png"));
        assertNotNull(Resource.getImage("resources/numbers.png"));
        assertNotNull(Resource.getImage("resources/intro-text.png"));
        assertNotNull(Resource.getImage("resources/game-over.png"));
        assertNotNull(Resource.getImage("resources/replay.png"));
    }
    
}
