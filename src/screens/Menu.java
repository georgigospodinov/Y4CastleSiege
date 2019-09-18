package screens;

import screens.components.Button;
import screens.components.Label;

import static main.Run.P;
import static main.ScreenManager.sm;

public class Menu extends AScreen {

    private static final float QUEST_X = sm.getWidth() * P.getFloat("label quest x");
    private static final float QUEST_Y = sm.getHeight() * P.getFloat("label quest y");
    private static final float QUEST_SIZE = sm.getHeight() * P.getFloat("label quest size");

    private static final float PLAY_X = sm.getWidth() * P.getFloat("button play x");
    private static final float PLAY_Y = sm.getHeight() * P.getFloat("button play y");
    private static final float PLAY_SIZE = sm.getHeight() * P.getFloat("button play size");

    private static final float CONTROLS_X = sm.getWidth() * P.getFloat("button controls x");
    private static final float CONTROLS_Y = sm.getHeight() * P.getFloat("button controls y");
    private static final float CONTROLS_SIZE = sm.getHeight() * P.getFloat("button controls size");

    private static final float LORE_X = sm.getWidth() * P.getFloat("button lore x");
    private static final float LORE_Y = sm.getHeight() * P.getFloat("button lore y");
    private static final float LORE_SIZE = sm.getHeight() * P.getFloat("button lore size");

    static {
        sm.textFont(sm.createFont("Fira Sans Bold", 15));
    }

    public Menu() {
        Label quest = new Label("Conquer all the castles!", QUEST_X, QUEST_Y, QUEST_SIZE);
        labels.add(quest);

        Button play = new Button("TO BATTLE!", PLAY_X, PLAY_Y, PLAY_SIZE);
        play.onClick(() -> sm.setScreen(new Play()));
        clickables.add(play);

        Button controls = new Button("How do I conquer castles?", CONTROLS_X, CONTROLS_Y, CONTROLS_SIZE);
        controls.onClick(() -> sm.setScreen(new Controls()));
        clickables.add(controls);

        Button lore = new Button("What is this about?", LORE_X, LORE_Y, LORE_SIZE);
        lore.onClick(() -> sm.setScreen(new Lore()));
        clickables.add(lore);
    }

}
