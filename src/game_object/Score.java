package game_object;

import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import util.Resource;

public class Score {

	private static final String SCORE_FILE = "best-scores.txt";

	// incremento do placar a cada frame
	private static final double SCORE_INC = 0.1;
	// numero maximo de caracteres do placar, limite de pontucao eh de 99999 (boa
	// sorte para quem quiser tentar)
	private static final int SCORE_LENGTH = 5;
	// largura em pixeis de cada algarismo
	private static final int NUMBER_WIDTH = 20;

	// posicoes do placar na tela
	private static final int CURRENT_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100);
	private static final int HI_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2;
	private static final int HI_X = SCREEN_WIDTH
			- ((SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2 + NUMBER_WIDTH * 2 + SCREEN_WIDTH / 100);
	private static final int SCORE_Y = SCREEN_HEIGHT / 25;

	private File scoreFile;
	private BufferedImage hi;
	private BufferedImage[] numbers;

	private double score;
	private int hiScore;

	public Score() {

		// inicializa placar zerado
		score = 0;

		// carrega arquivo do highscore
		scoreFile = new File(Paths.get(Resource.RESOURCES_PATH, SCORE_FILE).toString());
		readScore();

		hi = Resource.HI_SPRITE;
		numbers = Resource.NUMBERS_SPRITE;
	}

	public double getScore() {
		return score;
	}

	public int getHiScore() {
		return hiScore;
	}

	public void scoreUp() {
		score += SCORE_INC;
	}

	// funcao utilitaria para converter numero em array de algarismos
	private int[] scoreToArray(double scoreType) {
		int scoreArray[] = new int[SCORE_LENGTH];
		int tempScore = (int) scoreType;
		for (int i = 0; i < SCORE_LENGTH; i++) {
			int number = tempScore % 10;
			tempScore = (tempScore - number) / 10;
			scoreArray[i] = number;
		}
		return scoreArray;
	}

	// escreve placar no arquivo
	public void writeScore() {
		if (score > hiScore) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(scoreFile))) {
				bw.write(Integer.toString((int) score));
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// le placar do arquivo
	public void readScore() {
		if (scoreFile.exists()) {
			String line = "";
			if (scoreFile.exists()) {
				try (BufferedReader br = new BufferedReader(new FileReader(scoreFile))) {
					while ((line = br.readLine()) != null) {
						hiScore = Integer.parseInt(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else
			hiScore = (int) score;
	}

	public void scoreReset() {
		if (score > hiScore)
			hiScore = (int) score;
		score = 0;
	}

	// desenha placar na tela
	public void draw(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int scoreArray[] = scoreToArray(score);

		// desenha placar do jogo atual
		for (int i = 0; i < SCORE_LENGTH; i++) {
			g2d.drawImage(numbers[scoreArray[SCORE_LENGTH - i - 1]], CURRENT_SCORE_X + i * NUMBER_WIDTH, SCORE_Y, null);
		}

		// desenha placar mais alto com transparencia em 50%
		if (hiScore > 0) {

			int hiScoreArray[] = scoreToArray(hiScore);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

			for (int i = 0; i < SCORE_LENGTH; i++) {
				g2d.drawImage(numbers[hiScoreArray[SCORE_LENGTH - i - 1]], HI_SCORE_X + i * NUMBER_WIDTH, SCORE_Y,
						null);
			}

			g2d.drawImage(hi, HI_X, SCORE_Y, null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}

}
