package user_interface;

import javax.swing.JFrame;
import java.awt.Toolkit;

public class GameWindow extends JFrame {
	
	public static final int SCREEN_WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int SCREEN_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	private GameScreen gameScreen;
	
	public GameWindow() {
		super("Dino");

		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		gameScreen = new GameScreen();
		add(gameScreen);
	}
	
	public void startGame() {
		gameScreen.startThread();
	}	
}
