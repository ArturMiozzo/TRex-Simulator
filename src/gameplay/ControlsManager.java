package gameplay;

import user_interface.GameScreen;

public class ControlsManager {

	Controls controls;
	GameScreen gameScreen;
	
	// classe que administra os controles do jogo 
	public ControlsManager(Controls controls, GameScreen gameScreen) {
		this.controls = controls;
		this.gameScreen = gameScreen;
	}
		
	// dispara acoes pelos controles 
	public void update() {
		if(controls.isPressedUp())
			gameScreen.pressUpAction();
		if(controls.isPressedDown())
			gameScreen.pressDownAction();
	}
	
}
