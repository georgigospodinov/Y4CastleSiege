package screens;

import general.Player;
import screens.components.Button;
import screens.components.Message;

import static main.Run.P;
import static main.ScreenManager.player;
import static main.ScreenManager.sm;

public class GameOver extends AScreen {

    private static final float BUTTON_GAME_OVER_X = sm.getWidth() * P.getFloat("button game over x");
    private static final float BUTTON_GAME_OVER_Y = sm.getHeight() * P.getFloat("button game over y");
    private static final float BUTTON_GAME_OVER_SIZE = sm.getHeight() * P.getFloat("button game over size");

    private static final float STATS_GAME_OVER_X = sm.getWidth() * P.getFloat("stats game over x");
    private static final float STATS_GAME_OVER_Y = sm.getHeight() * P.getFloat("stats game over y");
    private static final float STATS_GAME_OVER_SIZE = sm.getHeight() * P.getFloat("stats game over size");

    public GameOver() {
        Message score = new Message("Congratulations!!!", STATS_GAME_OVER_X, STATS_GAME_OVER_Y, STATS_GAME_OVER_SIZE);
        score.addLine("You have completed all the levels!");
        score.addLine("Moral: " + player.getMoral());

        labels.add(score);

        Button play = new Button("Back to main menu", BUTTON_GAME_OVER_X, BUTTON_GAME_OVER_Y, BUTTON_GAME_OVER_SIZE);
        play.onClick(() -> sm.setScreen(new Menu()));
        clickables.add(play);
        // Reset all progress.
        player = new Player();
    }
}
