package user_interface;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
	
	public static final int SCREEN_WIDTH = 1200;
	public static final int SCREEN_HEIGHT = 300;

	private GameScreen gameScreen;
	
	public GameWindow() {
		super("TRex Simulator");

		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		gameScreen = new GameScreen();
		add(gameScreen);
	}
	
	public void startGame() {
		gameScreen.startThread();
	}	
}
