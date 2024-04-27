import user_interface.GameWindow;

public class Main {
    public static void main(String[] args) {
        GameWindow gameWindow = new GameWindow();
        gameWindow.startGame();
        gameWindow.setVisible(true);
    }
}
