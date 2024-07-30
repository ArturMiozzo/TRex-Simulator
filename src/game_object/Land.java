package game_object;

import user_interface.GameScreen;
import util.Resource;

import static user_interface.GameWindow.SCREEN_HEIGHT;
import static user_interface.GameWindow.SCREEN_WIDTH;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Land extends Scenario {

	private double x = 0;
	private int y;
	
	// define tamanho em escala, 2x maior
	private int landWidthScaled;
	private int landHeightScaled;

	private BufferedImage land;

	// classe do chao
	public Land(GameScreen gameScreen) {
		super(gameScreen);
		land = Resource.LAND_SPRITE;
		y = SCREEN_HEIGHT - land.getHeight() * 2 - 4;
		landWidthScaled = land.getWidth() * 2;
		landHeightScaled = land.getHeight() * 2;
	}

	// atualiza posicao
	// desloca de acordo com a velocidade 
	@Override
	public void updatePosition() {
		x += Math.round(gameScreen.getSpeedX() * 100d) / 100d;
	}

	// reseta posicao
	public void resetLand() {
		x = 0;
	}

	// desenha o chao
	@Override
	public void draw(Graphics g) {

		g.drawImage(land, (int) x, y, landWidthScaled, landHeightScaled, null);
		
		// se cruzar a borda da tela, adiciona mais um sprite do lado
		// dando a impressao de nunca acabar
		if (landWidthScaled - SCREEN_WIDTH <= (int) Math.abs(x))
			g.drawImage(land, (int) (landWidthScaled + x), y, landWidthScaled, landHeightScaled, null);
		
		if (landWidthScaled <= (int) Math.abs(x))
			x = 0;
	}

}
