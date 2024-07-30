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

import util.Resource;

public class Score {
	
	// value by which score is increasing
	private static final double SCORE_INC = 0.1;
	// length of score on screen, max 99999 but i dont think that anyone will play that long so.....
	private static final int SCORE_LENGTH = 5;
	// width and height of single number on sprite
	private static final int NUMBER_WIDTH = 20;
	// here i calculate position of score on screen
	private static final int CURRENT_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100);
	private static final int HI_SCORE_X = SCREEN_WIDTH - (SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2;
	private static final int HI_X = SCREEN_WIDTH - ((SCORE_LENGTH * NUMBER_WIDTH + SCREEN_WIDTH / 100) * 2 + NUMBER_WIDTH * 2 + SCREEN_WIDTH / 100);
	private static final int SCORE_Y = SCREEN_HEIGHT / 25;
	
	private String scoreFileName;
	private File scoreFile;
	private BufferedImage hi;
	private BufferedImage[] numbers;
	
	private double score;
	private int hiScore;
	
	public Score() {
		score = 0;
		scoreFileName = "best-scores.txt";
		scoreFile = new File("resources/" + scoreFileName);
		readScore();
		hi = Resource.HI_SPRITE;
		numbers = Resource.NUMBERS_SPRITE;
	}
	
	public void scoreUp() {
		score += SCORE_INC;
	}
	
	private int[] scoreToArray(double scoreType) {
		int scoreArray[] = new int[SCORE_LENGTH];
		int tempScore = (int)scoreType;
		for(int i = 0; i < SCORE_LENGTH; i++) {
			int number = tempScore % 10;
			tempScore = (tempScore - number) / 10;
			scoreArray[i] = number;
		}
		return scoreArray;
	}
	
	public void writeScore() {
		if(score > hiScore) {
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(scoreFile, true))) {
				bw.write(Integer.toString((int)score));
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void readScore() {
		if(scoreFile.exists() || new File(ClassLoader.getSystemClassLoader().getResource("").getPath() + scoreFileName).exists()) {
			String line = "";
			if(scoreFile.exists()) {				
				try(BufferedReader br =  new BufferedReader(new FileReader(scoreFile))) {
					while((line = br.readLine()) != null) {
						hiScore = Integer.parseInt(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else
			hiScore = (int)score;
	}
	
	public void scoreReset() {
		if(score > hiScore)
			hiScore = (int)score;
		score = 0;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		int scoreArray[] = scoreToArray(score);
		for(int i = 0; i < SCORE_LENGTH; i++) {
			g2d.drawImage(numbers[scoreArray[SCORE_LENGTH - i - 1]], CURRENT_SCORE_X + i * NUMBER_WIDTH, SCORE_Y, null);
		}
		if(hiScore > 0) {
			int hiScoreArray[] = scoreToArray(hiScore);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			for(int i = 0; i < SCORE_LENGTH; i++) {
				g2d.drawImage(numbers[hiScoreArray[SCORE_LENGTH - i - 1]], HI_SCORE_X + i * NUMBER_WIDTH, SCORE_Y, null);
			}
			g2d.drawImage(hi, HI_X, SCORE_Y, null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}
	
}
