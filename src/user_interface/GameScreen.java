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
import gameplay.ControlsManager;
import gameplay.EnemyManager;
import util.Resource;

public class GameScreen extends JPanel implements Runnable {

	private Thread thread;

	//velocidade inicial, como o mapa anda da esquerda para a direita, é negativo
	private static final int STARTING_SPEED_X = -5;

	//incremento da velocidade (dificuldade) a cada ponto
	private static final double DIFFICULTY_INC = -0.0002;

	//gravidade que define o quanto o dino cai a cada frame
	public static final double GRAVITY = 0.4;

	//posição Y do chão
	public static final int GROUND_Y = 280;

	//velocidade vertical, usada no pulo do dino
	public static final double SPEED_Y = -12;

	//fps desejado
	private final int FPS = 100;

	//numero de nanosegundos para atualizar o frame
	private final int NS_PER_FRAME = 1_000_000_000 / FPS;

	private double speedX = STARTING_SPEED_X;
	private GameState gameState = GameState.START;
	private boolean introJump = true;

	private Controls controls;
	private Score score;
	private Dino dino;
	private Land land;
	private Clouds clouds;
	private EnemyManager enemyManager;
	private ControlsManager controlsManager;

	public GameScreen() {
		
		// cria thread da tela
		thread = new Thread(this);

		// cria classe para os controles do jogo 
		controls = new Controls(this);

		// envia para a lib grafica quais os botoes queremos mapear 
		super.add(controls.pressUp);
		super.add(controls.releaseUp);
		super.add(controls.pressDown);
		super.add(controls.releaseDown);
		
		// inicializa classe que envia os controles para a tela
		controlsManager = new ControlsManager(controls, this);
		
		// inicializa placar
		score = new Score();

		// inicializa personagem principal
		dino = new Dino(controls);

		// inicializa chao
		land = new Land(this);

		// inicializa nuvens
		clouds = new Clouds(this);

		// inicializa classe que controla os inimigos e colisões
		enemyManager = new EnemyManager(this);
	}

	// inicia thread da tela
	public void startThread() {
		thread.start();
	}

	// override da função de execução da tela,
	// ela roda um loop responsavel por atualizar a posição dos objetos do jogo
	// a cada execução fica congelada o tempo necessario para manter o FPS
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
				
			// espera 1 segundo antes de reiniciar para não correr risco de reiniciar sem querer
			// por conta de alguma tecla no buffer
			if (gameState == GameState.END)
				waitingTime = 1000;
			
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

	// atualiza posições de cada objeto do jogo
	private void updateFrame() {

		switch (gameState) {

			// se estiver na tela inicial, executa um pulo e começa o jogo
			case INTRO:
				dino.updatePosition();				
				clouds.updatePosition();

				if (!introJump && dino.getDinoState() == DinoState.DINO_RUN)
					land.updatePosition();

				gameState = GameState.IN_PROGRESS;
				
				if (introJump) {
					dino.jump();
					dino.setDinoState(DinoState.DINO_JUMP);
					introJump = false;
				}
				break;
			
				
			// se estiver na execução do jogo
			// incrementa a velocidade
			// e verifica se teve alguma colisão
			// encerra o jogo se necessario
			case IN_PROGRESS:

				speedX += DIFFICULTY_INC;
				
				dino.updatePosition();
				land.updatePosition();
				clouds.updatePosition();
				enemyManager.updatePosition();

				if (enemyManager.isCollision(dino.getHitbox())) {
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

	// override da função paint da tela
	// pinta o fundo de branco
	// e desenha os objetos dependendo do estado do jogo
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

	// Desenha tela inicial, com o dino, o chao e a mensagem de inicio no centro
	private void startScreen(Graphics g) {
		land.draw(g);
		dino.draw(g);
		BufferedImage introImage = Resource.INTRO_SPRITE;
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(introImage, SCREEN_WIDTH / 2 - introImage.getWidth() / 2,
				SCREEN_HEIGHT / 2 - introImage.getHeight(), null);
	}
	
	// Desenha tela de introdução, adicionando nuvens a tela de inicio
	// serve para fazer uma animação de inicio
	private void introScreen(Graphics g) {
		clouds.draw(g);
		startScreen(g);
	}

	// Tela com jogo em progresso, desenha todos os objetos do jogo
	private void inProgressScreen(Graphics g) {
		clouds.draw(g);
		land.draw(g);
		enemyManager.draw(g);
		dino.draw(g);
		score.draw(g);
	}

	// Desenha tela de gameover, com mensagem de gameover e reinicio
	// fica por cima da tela de jogo em progresso
	private void gameOverScreen(Graphics g) {
		inProgressScreen(g);
		BufferedImage gameOverImage = Resource.GAME_OVER_SPRITE;
		BufferedImage replayImage = Resource.REPLAY_SPRITE;
		g.drawImage(gameOverImage, SCREEN_WIDTH / 2 - gameOverImage.getWidth() / 2,
				SCREEN_HEIGHT / 2 - gameOverImage.getHeight() * 2, null);
		g.drawImage(replayImage, SCREEN_WIDTH / 2 - replayImage.getWidth() / 2, SCREEN_HEIGHT / 2, null);
	}

	// Callback da acao de apertar para cima
	// executa um pulo
	public void pressUpAction() {
		if (gameState == GameState.IN_PROGRESS) {
			dino.jump();
			dino.setDinoState(DinoState.DINO_JUMP);
		}
	}

	// Callback da acao de soltar a tecla de cima
	// inicia ou reinicia o jogo
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
	
	// Callback da acao de apertar para baixo
	// Faz o dino abaixar
	public void pressDownAction() {
		if (dino.getDinoState() != DinoState.DINO_JUMP && gameState == GameState.IN_PROGRESS)
			dino.setDinoState(DinoState.DINO_DOWN_RUN);
	}

	// Callback da acao de soltar para baixo
	// Faz o dino levantar
	public void releaseDownAction() {
		if (dino.getDinoState() != DinoState.DINO_JUMP && gameState == GameState.IN_PROGRESS)
			dino.setDinoState(DinoState.DINO_RUN);
	}
}
