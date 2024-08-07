package gameplay;

import user_interface.GameScreen;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class Controls {

	private static final int FOCUS_STATE = JComponent.WHEN_IN_FOCUSED_WINDOW;
	
	private static final String UP = "UP";
	private static final String DOWN = "DOWN";
	private static final String SPACE_UP = "SPACE_UP";
	
	private static final String RELEASED_UP = "RELEASED_UP";
	private static final String RELEASED_DOWN = "RELEASED_DOWN";
	private static final String RELEASED_SPACE_UP = "RELEASED_SPACE_UP";
	
	public JLabel pressUp = new JLabel();
	public JLabel releaseUp = new JLabel();
	public JLabel pressDown = new JLabel();
	public JLabel releaseDown = new JLabel();
	
	private boolean isPressedUp = false;
	private boolean isPressedDown = false;
	
	GameScreen gameScreen;
	
	// registra controles do jogo
	public Controls(GameScreen gameScreen) {

		this.gameScreen = gameScreen;

		// Seta para cima
		// aperta
		pressUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("UP"), UP);
		pressUp.getActionMap().put(UP, new PressUpAction());
		//solta
		releaseUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("released UP"), RELEASED_UP);
		releaseUp.getActionMap().put(RELEASED_UP, new ReleaseUpAction());

		// espaço
		// aperta
		pressUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("SPACE"), SPACE_UP);
		pressUp.getActionMap().put(SPACE_UP, new PressUpAction());
		//solta
		releaseUp.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("released SPACE"), RELEASED_SPACE_UP);
		releaseUp.getActionMap().put(RELEASED_SPACE_UP, new ReleaseUpAction());

		// seta para baixo
		// aperta
		pressDown.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("DOWN"), DOWN);
		pressDown.getActionMap().put(DOWN, new PressDownAction());
		//solta
		releaseDown.getInputMap(FOCUS_STATE).put(KeyStroke.getKeyStroke("released DOWN"), RELEASED_DOWN);
		releaseDown.getActionMap().put(RELEASED_DOWN, new ReleaseDownAction());
	}
	
	public boolean isPressedUp() {
		return isPressedUp;
	}

	public boolean isPressedDown() {
		return isPressedDown;
	}

	private class PressUpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			isPressedUp = true;
		}
	}
	
	private class ReleaseUpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			gameScreen.releaseUpAction();
			isPressedUp = false;
		}
	}
	
	private class PressDownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			isPressedDown = true;
		}
	}
	
	private class ReleaseDownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			gameScreen.releaseDownAction();
			isPressedDown = false;
		}
	}	
}
