package game_object;

import user_interface.GameScreen;
import util.Resource;

import static user_interface.GameScreen.GROUND_Y;
import static user_interface.GameWindow.SCREEN_WIDTH;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gameplay.EnemyManager;

public class Cactuses extends Enemy {
	
	// Classe que representa os cactus
	private class Cactus {
		
		private BufferedImage cactusImage;
		private double x;
		private int y;
		
		private Cactus(BufferedImage cactusImage, double x, int y) {
			this.cactusImage = cactusImage;
			this.x = x;
			this.y = y;
		}

		public void setX(double x) {
			this.x = x;
		}

	}
	
	// 'escala' utiliza nas hitbox dos cactus
	private static final double HITBOX_X = 2.7;
	private static final int HITBOX_Y = 25;

	// max number of cactuses grouped
	private static final int MAX_CACTUS_GROUP = 3;
	
	private List<Cactus> cactuses;
	
	public Cactuses(GameScreen gameScreen, EnemyManager enemyManager) {
		super(gameScreen, enemyManager);
		cactuses = new ArrayList<Cactus>();
	}

	public void setX(double x) {
		for (Cactus cactus : cactuses) {
			cactus.setX(x);
		}
	}

	public List<Cactus> getCactusesList() {
		return cactuses;
	}

	// atualiza posicao dos cactus na mesma velocidade da tela
	@Override
	public void updatePosition() {
		for(Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			
			Cactus cactus = i.next();
			cactus.x += Math.round(gameScreen.getSpeedX() * 100d) / 100d;

			// se saiu da tela, remove da lista
			if ((int) cactus.x + cactus.cactusImage.getWidth() < 0) {
				i.remove();
			}
		}
	}
	
	// verifica se existe espaço na tela para adicionar outro cactus
	@Override
	public boolean spaceAvailable() {
		for (Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			Cactus cactus = i.next();
			if (SCREEN_WIDTH - (cactus.x + cactus.cactusImage.getWidth()) < enemyManager.getDistanceBetweenEnemies()) {
				return false;
			}
		}
		return true;
	}

	// adiciona um cactus a lista
	// existe uma chance de dar spaw neles, que eh incrementada junto com a dificuldade
	// pode spawnar ate MAX_CACTUS_GROUP por vez, um ao lado do outro
	public boolean createCactuses() {

		if(Math.random() * 100 < enemyManager.getCactusesPercentage()) {

			for(int i = 0, numberOfCactuses = (int)(Math.random() * MAX_CACTUS_GROUP + 1); i < numberOfCactuses; i++) {
				
				// escolhe um sprite aleatorio
				BufferedImage cactusImage = Resource.CACTUS_SPRITE[(int)(Math.random() * Resource.CACTUS_SPRITE.length)];

				// a posicao inicial eh no chao e no canto direito da tela
				int x = SCREEN_WIDTH;
				int y = GROUND_Y - cactusImage.getHeight();

				// se for adiacionar mais de 1 por vez, adiciona no x para nao ficar um em cima do outro
				if (i > 0)
					x = (int) cactuses.get(cactuses.size() - 1).x
							+ cactuses.get(cactuses.size() - 1).cactusImage.getWidth();

				cactuses.add(new Cactus(cactusImage, x, y));
			}
			return true;
		}
		return false;
	}
	
	// detecta colisao
	@Override
	public boolean isCollision(Rectangle dinoHitBox) {
		for (Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			Cactus cactus = i.next();
			Rectangle cactusHitBox = getHitbox(cactus);
			if (cactusHitBox.intersects(dinoHitBox))
				return true;
		}
		return false;
	}
	
	// calcula hitbox do cactus
	private Rectangle getHitbox(Cactus cactus) {

		// a hitbox eh calculada pela base + o tamanho do sprite sobre uma escala empirica kkk
		// ela fica um pouco menor que o sprite, facilitando um pouco
		return new Rectangle(
			(int)cactus.x + (int)(cactus.cactusImage.getWidth() / HITBOX_X),
			cactus.y + cactus.cactusImage.getHeight() / HITBOX_Y, 
			cactus.cactusImage.getWidth() - (int)(cactus.cactusImage.getWidth() / HITBOX_X) * 2,
			cactus.cactusImage.getHeight() - cactus.cactusImage.getHeight() / HITBOX_Y
		);

	}
	
	// remove os cactus
	public void clearCactuses() {
		cactuses.clear();
	}

	// desenha os cactus
	@Override
	public void draw(Graphics g) {
		for (Iterator<Cactus> i = cactuses.iterator(); i.hasNext();) {
			Cactus cactus = i.next();
			g.drawImage(cactus.cactusImage, (int) (cactus.x), cactus.y, null);
		}
	}
}
