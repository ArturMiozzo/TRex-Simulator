package game_object;

import user_interface.GameScreen;
import util.Resource;

import static user_interface.GameScreen.GROUND_Y;
import static user_interface.GameWindow.SCREEN_WIDTH;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gameplay.Animation;
import manager.EnemyManager;

public class Birds extends Enemy {

	// Classe que representa os piterodatilos
	private class Bird {

		private double x;
		private int y;
		private Animation birdFly;

		private Bird(double x, int y, Animation birdFly) {
			this.x = x;
			this.y = y;
			this.birdFly = birdFly;
		}
	}

	// diferença de altura dos sprites usados, eh necessaria para que o passaro fique sempre centralizado
	private static final int HITBOX_MODELS_DIFF_IN_Y = -12;
	
	// Hitbox quando o bird esta com as azas para cima
	// A hitbox segue o padrao considerando o centro e a altura do objeto e deslocando em x e y
	private static final int[] HITBOX_WINGS_UP = { 20, 4, -40, -20 };
	
	// Hitbox quando o bird esta com as azas para baixo
	private static final int[] HITBOX_WINGS_DOWN = { 20, 4, -40, -28 };

	private List<Bird> birds;

	public Birds(GameScreen gameScreen, EnemyManager enemyManager) {
		super(gameScreen, enemyManager);
		birds = new ArrayList<Bird>();
	}

	// função de atualizar a posicao dos birds
	@Override
	public void updatePosition() {
		for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = i.next();
			// aumenta a velocidade dos birds em 20% da velocidade do jogo
			bird.x += (gameScreen.getSpeedX() + gameScreen.getSpeedX() / 5);
			bird.birdFly.updateSprite();
		}
	}

	// verifica se existe espaço na tela para adicionar outro bird
	@Override
	public boolean spaceAvailable() {
		for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = i.next();
			if (SCREEN_WIDTH - (bird.x + bird.birdFly.getSprite().getWidth()) < enemyManager.getDistanceBetweenEnemies()) {
				return false;
			}
		}
		return true;
	}

	// adiciona um bird a lista
	// existe uma chance de dar spaw neles, que eh incrementada junto com a dificuldade
	// ao criar, define a posicao Y dele aleatoriamente
	public boolean createBird() {
		if (Math.random() * 100 < enemyManager.getBirdsPercentage()) {
			Animation birdFly = new Animation(400);
			birdFly.addSprite(Resource.BIRD_SPRITE[0]);
			birdFly.addSprite(Resource.BIRD_SPRITE[1]);
			birds.add(new Bird(SCREEN_WIDTH, (int) (Math.random() * (GROUND_Y - birdFly.getSprite().getHeight())),
					birdFly));
			return true;
		}
		return false;
	}

	// detecta colisao do player com algum bird
	@Override
	public boolean isCollision(Rectangle dinoHitBox) {
		for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = i.next();
			Rectangle birdHitBox = getHitbox(bird);
			if (birdHitBox.intersects(dinoHitBox))
				return true;
		}
		return false;
	}

	// calcula hitbox do bird
	private Rectangle getHitbox(Bird bird) {

		int[] hitbox = bird.birdFly.getSpriteIndex() == 0 ? HITBOX_WINGS_UP : HITBOX_WINGS_DOWN;

		return new Rectangle(
			// x eh a posicao + o deslocamento da hitbox
			(int) bird.x + hitbox[0],
			// y eh a posicao + o deslocamento da hitbox
			bird.y + hitbox[1],
			// largura eh a largura do sprite + o deslocamento da hitbox
			bird.birdFly.getSprite().getWidth() + hitbox[2],
			// altura eh a altura do sprite + o deslocamento da hitbox
			bird.birdFly.getSprite().getHeight() + hitbox[3]
		);

	}

	// remove todos os birds
	public void clearBirds() {
		birds.clear();
	}
	
	// funcao para desenhar os birds
	@Override
	public void draw(Graphics g) {
		for (Iterator<Bird> i = birds.iterator(); i.hasNext();) {
			Bird bird = (Bird) i.next();
			g.drawImage(
				bird.birdFly.getSprite(),
				(int) bird.x,
				// se for o sprite com azas para cima, aplica o deslocamento
				bird.birdFly.getSpriteIndex() == 1 ? bird.y + HITBOX_MODELS_DIFF_IN_Y : bird.y,
				null);
		}
	}
}
