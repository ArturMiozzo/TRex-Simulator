package tests;

import org.junit.*;
import static org.junit.Assert.*;

import java.awt.Rectangle;

import enumeration.*;
import game_object.*;
import gameplay.*;
import user_interface.*;

public class DinoTest {
    GameScreen gameScreen;
    Controls controls = new Controls(gameScreen);
    Dino dino = new Dino(controls);

    @Test
    public void testInitialState() {
        assertEquals(DinoState.DINO_JUMP, dino.getDinoState());
    }

    @Test
    public void testHitboxRunState() {
        dino.setDinoState(DinoState.DINO_RUN);
        Rectangle hitbox = dino.getHitbox();
        assertNotNull(hitbox);
    }

    @Test
    public void testHitboxDownRunState() {
        dino.setDinoState(DinoState.DINO_DOWN_RUN);
        Rectangle hitbox = dino.getHitbox();
        assertNotNull(hitbox);
    }

    @Test
    public void testDinoDead() {
        dino.dinoGameOver();
        assertEquals(DinoState.DINO_DEAD, dino.getDinoState());
    }

    @Test
    public void testResetDino() {
        dino.dinoGameOver();
        dino.resetDino();
        assertEquals(DinoState.DINO_RUN, dino.getDinoState());
    }
}
