package game_object;

import user_interface.GameScreen;
import util.Resource;

import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Clouds extends Scenario {

	// Classe que representa as nuvens
	private class Cloud {

		private BufferedImage cloudImage;
		private double x;
		private int y;

		private Cloud(BufferedImage cloudImage, double x, int y) {
			this.cloudImage = cloudImage;
			this.x = x;
			this.y = y;
		}

	}

	// numero maximo de nuvens na tela por vez
	private static final int CLOUDS_AMOUNT = 5;
	// chance de spawnar uma nuvem
	private static final double CLOUD_PERCENTAGE = 0.4;

	private Set<Cloud> clouds;
	
	// tamanho da nuvem escalado em 2x
	private int cloudWidthScaled;
	private int cloudHeightScaled;

	public Clouds(GameScreen gameScreen) {
		super(gameScreen);
		clouds = new HashSet<Cloud>();
		cloudWidthScaled = Resource.CLOUD_SPRITE.getWidth() * 2;
		cloudHeightScaled = Resource.CLOUD_SPRITE.getHeight() * 2;
	}

	// atualiza posicao
	// remove as que estao fora da tela e cria novas
	@Override
	public void updatePosition() {
		
		for (Iterator<Cloud> i = clouds.iterator(); i.hasNext();) {
			
			Cloud cloud = (Cloud) i.next();
			
			// aumenta velocidade de acordo com a velocidade da tela
			cloud.x += gameScreen.getSpeedX() / 7;
			
			// remove se a x for negativo
			if (cloud.x + cloudWidthScaled < 0) {
				i.remove();
			}
		}

		createClouds();
	}

	// cria nuvens se estiver sobrando espaco
	private void createClouds() {

		if (clouds.size() < CLOUDS_AMOUNT) {
		
			for (Iterator<Cloud> i = clouds.iterator(); i.hasNext();) {
				Cloud temp = (Cloud) i.next();
				if (temp.x >= SCREEN_WIDTH - cloudWidthScaled)
					return;
			}
		
			if (Math.random() * 100 < CLOUD_PERCENTAGE)
				clouds.add(new Cloud(Resource.CLOUD_SPRITE, SCREEN_WIDTH,
						(int) (Math.random() * (SCREEN_HEIGHT / 2))));
		}
	}

	// remove todas as nuvens
	public void clearClouds() {
		clouds.clear();
	}

	// desenha as nuvens
	@Override
	public void draw(Graphics g) {
		for (Iterator<Cloud> i = clouds.iterator(); i.hasNext();) {
			Cloud cloud = (Cloud) i.next();
			g.drawImage(cloud.cloudImage, (int) cloud.x, cloud.y, cloudWidthScaled, cloudHeightScaled, null);
		}
	}
}
