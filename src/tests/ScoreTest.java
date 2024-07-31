package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import game_object.Score;

public class ScoreTest {
    private Score score;

    @Before
    public void setUp() throws IOException {

        // Inicializa a classe Score
        score = new Score();
    }

    @Test
    public void testScoreInitialization() {
        // Verifica se a pontuação inicial é zero
        assertEquals(0, score.getScore(), 0.1);
    }

    @Test
    public void testScoreUp() {
        // Testa a atualização da pontuação
        score.scoreUp();
        assertEquals(0.1, score.getScore(), 0.1);
    }

    @Test
    public void testScoreReset() {
        // Testa o reset da pontuação
        score.scoreUp();
        score.scoreReset();
        assertEquals(0, score.getScore(), 0.1);
    }
}
