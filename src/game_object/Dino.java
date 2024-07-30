package game_object;

import static user_interface.GameScreen.GRAVITY;
import static user_interface.GameScreen.GROUND_Y;
import static user_interface.GameScreen.SPEED_Y;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import enumeration.DinoState;
import gameplay.Animation;
import gameplay.Controls;
import util.Resource;

public class Dino {
	
	// hitbox, segue o padrao considerando o centro e a altura do objeto e deslocando em x e y
	private static final int[] HITBOX_RUN = {12, 26, -32, -42};
	private static final int[] HITBOX_DOWN_RUN = {24, 8, -60, -24};
	
	// posicao inicial do dino
	public static final double X = 120;
	
	Controls controls;
	
	private double maxY;
	private double highJumpMaxY;
	private double lowJumpMaxY;
	
	private double y = 0;
	private double speedY = 0;
	
	private DinoState dinoState;
	private BufferedImage dinoJump;
	private BufferedImage dinoDead;
	private Animation dinoRun;
	private Animation dinoDownRun;
	
	public Dino(Controls controls) {
		this.controls = controls;

		// cria animacao do dino correndo		
		dinoRun = new Animation(150);
		for(BufferedImage dinoRunSprite : Resource.DINO_RUN_SPRITE) {
			dinoRun.addSprite(dinoRunSprite);
		}
		
		// cria animacao do dino correndo abaixado
		dinoDownRun = new Animation(150);
		for(BufferedImage dinoDownRunSprite : Resource.DINO_RUN_DOWN_SPRITE) {
			dinoDownRun.addSprite(dinoDownRunSprite);
		}

		dinoJump = Resource.DINO_JUMP_SPRITE;
		dinoDead = Resource.DINO_DEAD_SPRITE;

		// define Y com relação ao chao
		y = GROUND_Y - dinoJump.getHeight();
		maxY = y;
		
		// calcula altura maxima do pulo
		highJumpMaxY = setJumpMaxY(GRAVITY);
		lowJumpMaxY = setJumpMaxY(GRAVITY + GRAVITY / 2);
		
		// começa o jogo com o dino pulando
		dinoState = DinoState.DINO_JUMP;
	}
	
	public DinoState getDinoState() {
		return dinoState;
	}

	public void setDinoState(DinoState dinoState) {
		this.dinoState = dinoState;
	}
	
	// calcula altura maxima do pulo baseado na gravidade
	// dada a velocidade Y, incrementa a posicao até que a gravidade zere a velocidade
	// funciona tanto para pular quanto para cair
	public double setJumpMaxY(double gravity) {

		speedY = SPEED_Y;
		y += speedY;
		double jumpMaxY = y;
		
		while(true) {
			speedY += gravity;
			y += speedY;
			if(y < jumpMaxY)
				jumpMaxY = y;
			if(y + speedY >= GROUND_Y - dinoRun.getSprite().getHeight()) {
				speedY = 0;
				y = GROUND_Y - dinoRun.getSprite().getHeight();
				break;
			}
		}
		return jumpMaxY;
	}

	// calcula hitbox do dino
	public Rectangle getHitbox() {

		switch (dinoState) {
			// se estiver de peh, correndo ou pulando, calcula com a hitbox do dino correndo
			case DINO_RUN:
			case DINO_JUMP:
			case DINO_DEAD:
				return new Rectangle((int)X + HITBOX_RUN[0], (int)y + HITBOX_RUN[1], 
						dinoDead.getWidth() + HITBOX_RUN[2], dinoDead.getHeight() + HITBOX_RUN[3]);
			
			// se estiver abaixado, calcula com a hitbox do dino correndo abaixado
			case DINO_DOWN_RUN:
				return new Rectangle((int)X + HITBOX_DOWN_RUN[0], (int)y + HITBOX_DOWN_RUN[1], 
						dinoDownRun.getSprite().getWidth() + HITBOX_DOWN_RUN[2], dinoDownRun.getSprite().getHeight() + HITBOX_DOWN_RUN[3]);
		}
		return null;
	}
	
	// atualiza posicao do dino
	public void updatePosition() {

		if(y < maxY)
			maxY = y;
		
		// atualiza sprites
		dinoRun.updateSprite();
		dinoDownRun.updateSprite();
		
		switch (dinoState) {

			// define posicao Y fixa se estiver correndo no chao
			case DINO_RUN:
				y = GROUND_Y - dinoRun.getSprite().getHeight();
				maxY = y;
				break;

			case DINO_DOWN_RUN:
				y = GROUND_Y - dinoDownRun.getSprite().getHeight();
				break;

			// se estiver pulando
			case DINO_JUMP:

				// volta para o estado de correndo se estiver no chao
				// e fixa o Y
				if(y + speedY >= GROUND_Y - dinoRun.getSprite().getHeight()) {
					speedY = 0;
					y = GROUND_Y - dinoRun.getSprite().getHeight();
					dinoState = DinoState.DINO_RUN;
				} 
				// se começar a pular, soma a velocidade Y a gravidade
				else if(controls.isPressedUp()) {
					speedY += GRAVITY;
					y += speedY;
				}
				// se estiver pulando ou caindo
				// soma a gravidade na velocidade até os limites
				else {
					if(maxY <= lowJumpMaxY - (lowJumpMaxY - highJumpMaxY) / 2)
						speedY += GRAVITY;
					else
						speedY += GRAVITY + GRAVITY / 2;
					// se apertar para baixo ao cair
					// cai mais rapido
					if(controls.isPressedDown())
						speedY += GRAVITY;
					y += speedY;
				}
				break;

			default:
				break;
		}		
	}
	
	// começa a pular com o dino
	public void jump() {
		if(y == GROUND_Y - dinoRun.getSprite().getHeight()) {
			speedY = SPEED_Y;
			y += speedY;
		}
	}
	
	// reseta posição do dino
	public void resetDino() {
		y = GROUND_Y - dinoJump.getHeight();
		dinoState = DinoState.DINO_RUN;
	}
	
	// gera gameover
	public void dinoGameOver() {
		if(y > GROUND_Y - dinoDead.getHeight())
			y = GROUND_Y - dinoDead.getHeight();
		dinoState = DinoState.DINO_DEAD;
	}
	
	// desenha o dino baseado no estado
	public void draw(Graphics g) {
		switch (dinoState) {
		case DINO_RUN:
			g.drawImage(dinoRun.getSprite(), (int)X, (int)y, null);
			break;
		case DINO_DOWN_RUN:
			g.drawImage(dinoDownRun.getSprite(), (int)X, (int)y, null);
			break;
		case DINO_JUMP:
			g.drawImage(dinoJump, (int)X, (int)y, null);
			break;
		case DINO_DEAD:
			g.drawImage(dinoDead, (int)X, (int)y, null);
			break;
		default:
			break;
		}
	}
}
