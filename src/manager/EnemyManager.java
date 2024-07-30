package manager;

import game_object.Birds;
import game_object.Cactuses;
import user_interface.GameScreen;

import java.awt.Graphics;
import java.awt.Rectangle;

import enumeration.EnemyType;

public class EnemyManager {
	
	// Incremento da chance de spawn de acordo com a dificuldade
	private static final double PERCENTAGE_INC = 0.0001;

	// Decremento da distancia minima entre inimigos
	private static final double DISTANCE_DEC = -0.005;

	// distancia minima inicial
	private static final int MINIMUM_DISTANCE = 250;
	
	// distancia corrente entre inimigos
	private double distanceBetweenEnemies = 750;

	// chance de spawn de catus e birds
	private double cactusesPercentage = 2;
	private double birdsPercentage = 1;
	
	private Cactuses cactuses;
	private Birds birds;
	
	public EnemyManager(GameScreen gameScreen) {
		cactuses = new Cactuses(gameScreen, this);
		birds = new Birds(gameScreen, this);
	}
	
	public double getDistanceBetweenEnemies() {
		return distanceBetweenEnemies;
	}

	public double getCactusesPercentage() {
		return cactusesPercentage;
	}

	public double getBirdsPercentage() {
		return birdsPercentage;
	}

	// spawna inimigos aleatoriamente
	public void updatePosition() {
		
		// atualiza dificuldade
		cactusesPercentage += PERCENTAGE_INC;
		birdsPercentage += PERCENTAGE_INC;

		if(distanceBetweenEnemies > MINIMUM_DISTANCE)
			distanceBetweenEnemies += DISTANCE_DEC;

		cactuses.updatePosition();
		birds.updatePosition();
		
		// se tiver espaço, gera um inimigo aleatorio
		if(cactuses.spaceAvailable() && birds.spaceAvailable()) {
		
			switch (EnemyType.values()[(int)(Math.random() * EnemyType.values().length)]) {
				case CACTUS:
					if(cactuses.createCactuses())
						break;
				case BIRD:
					if(birds.createBird())
						break;
				default:
					cactuses.createCactuses();
					break;
			}
		}
	}
	
	// detecta colisao com inimigos
	public boolean isCollision(Rectangle hitBox) {
		if(cactuses.isCollision(hitBox) || birds.isCollision(hitBox))
			return true;
		return false;
	}
	
	// remove inimigos
	public void clearEnemy() {
		cactuses.clearCactuses();
		birds.clearBirds();
	}
		
	// desenha inimigos
	public void draw(Graphics g) {
		cactuses.draw(g);
		birds.draw(g);
	}
}
