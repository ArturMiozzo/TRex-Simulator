package user_interface;

import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import enumeration.DinoState;
import enumeration.GameState;
import game_object.Clouds;
import game_object.Dino;
import game_object.Land;
import game_object.Score;
import gameplay.Controls;
import manager.ControlsManager;
import manager.EnemyManager;
import util.Resource;

public class GameScreen extends JPanel implements Runnable {

	private Thread thread;

	private static final int STARTING_SPEED_X = -5;
	private static final double DIFFICULTY_INC = -0.0002;

	public static final double GRAVITY = 0.4;
	public static final int GROUND_Y = 280;
	public static final double SPEED_Y = -12;

	private final int FPS = 100;
	private final int NS_PER_FRAME = 1_000_000_000 / FPS;

	private double speedX = STARTING_SPEED_X;
	private GameState gameState = GameState.START;
	private boolean introJump = true;
	private boolean collisions = true;

	private Controls controls;
	private Score score;
	private Dino dino;
	private Land land;
	private Clouds clouds;
	private EnemyManager enemyManager;
	private ControlsManager controlsManager;

	public GameScreen() {
		thread = new Thread(this);
		controls = new Controls(this);
		super.add(controls.pressUp);
		super.add(controls.releaseUp);
		super.add(controls.pressDown);
		super.add(controls.releaseDown);
		
		controlsManager = new ControlsManager(controls, this);
		score = new Score();
		dino = new Dino(controls);
		land = new Land(this);
		clouds = new Clouds(this);
		enemyManager = new EnemyManager(this);
	}

	public void startThread() {
		thread.start();
	}

	@Override
	public void run() {
		long prevFrameTime = System.nanoTime();
		int waitingTime = 0;
		while (true) {
			controlsManager.update();
			updateFrame();
			repaint();
			waitingTime = (int) ((NS_PER_FRAME - (System.nanoTime() - prevFrameTime)) / 1_000_000);
			if (waitingTime < 0)
				waitingTime = 1;
				
			// little pause to not start new game if you are spamming your keys
			if (gameState == GameState.END)
				waitingTime = 300;
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			prevFrameTime = System.nanoTime();
		}
	}

	public double getSpeedX() {
		return speedX;
	}

	public GameState getGameState() {
		return gameState;
	}

	// update all entities positions
	private void updateFrame() {
		switch (gameState) {
			case INTRO:
				dino.updatePosition();
				if (!introJump && dino.getDinoState() == DinoState.DINO_RUN)
					land.updatePosition();
				clouds.updatePosition();
					gameState = GameState.IN_PROGRESS;
				if (introJump) {
					dino.jump();
					dino.setDinoState(DinoState.DINO_JUMP);
					introJump = false;
				}
				break;
			case IN_PROGRESS:
				speedX += DIFFICULTY_INC;
				dino.updatePosition();
				land.updatePosition();
				clouds.updatePosition();
				enemyManager.updatePosition();
				if (collisions && enemyManager.isCollision(dino.getHitbox())) {
					gameState = GameState.END;
					dino.dinoGameOver();
					score.writeScore();
				}
				score.scoreUp();
				break;
			default:
				break;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(246, 246, 246));
		g.fillRect(0, 0, getWidth(), getHeight());
		switch (gameState) {
			case START:
				startScreen(g);
				break;
			case INTRO:
				introScreen(g);
				break;
			case IN_PROGRESS:
				inProgressScreen(g);
				break;
			case END:
				gameOverScreen(g);
				break;
			default:
				break;
		}
	}

	private void startScreen(Graphics g) {
		land.draw(g);
		dino.draw(g);
		BufferedImage introImage = Resource.INTRO_SPRITE;
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(introImage, SCREEN_WIDTH / 2 - introImage.getWidth() / 2,
				SCREEN_HEIGHT / 2 - introImage.getHeight(), null);
	}

	private void introScreen(Graphics g) {
		clouds.draw(g);
		startScreen(g);
	}

	private void inProgressScreen(Graphics g) {
		clouds.draw(g);
		land.draw(g);
		enemyManager.draw(g);
		dino.draw(g);
		score.draw(g);
	}

	private void gameOverScreen(Graphics g) {
		inProgressScreen(g);
		BufferedImage gameOverImage = Resource.GAME_OVER_SPRITE;
		BufferedImage replayImage = Resource.REPLAY_SPRITE;
		g.drawImage(gameOverImage, SCREEN_WIDTH / 2 - gameOverImage.getWidth() / 2,
				SCREEN_HEIGHT / 2 - gameOverImage.getHeight() * 2, null);
		g.drawImage(replayImage, SCREEN_WIDTH / 2 - replayImage.getWidth() / 2, SCREEN_HEIGHT / 2, null);
	}

	public void pressUpAction() {
		if (gameState == GameState.IN_PROGRESS) {
			dino.jump();
			dino.setDinoState(DinoState.DINO_JUMP);
		}
	}

	public void releaseUpAction() {
		if (gameState == GameState.START)
			gameState = GameState.INTRO;
		if (gameState == GameState.END) {
			speedX = STARTING_SPEED_X;
			score.scoreReset();
			enemyManager.clearEnemy();
			dino.resetDino();
			clouds.clearClouds();
			land.resetLand();
			gameState = GameState.IN_PROGRESS;
		}
	}

	public void pressDownAction() {
		if (dino.getDinoState() != DinoState.DINO_JUMP && gameState == GameState.IN_PROGRESS)
			dino.setDinoState(DinoState.DINO_DOWN_RUN);
	}

	public void releaseDownAction() {
		if (dino.getDinoState() != DinoState.DINO_JUMP && gameState == GameState.IN_PROGRESS)
			dino.setDinoState(DinoState.DINO_RUN);
	}
}
