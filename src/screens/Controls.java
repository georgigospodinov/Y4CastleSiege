package screens;

import screens.components.Button;
import screens.components.Message;

import static main.ScreenManager.sm;

public class Controls extends AScreen {

    public Controls() {
        Message m = new Message("Click on a type of soldier to pick 'em up.", DETAILS_X, DETAILS_Y, DETAILS_SIZE);
        m.addLine("Drop 'em at the lower end of the screen.");
        m.addLine("They'll attack on their own.");
        m.addLine("Take care, though, your units are not unlimited!");
        m.addLine("You can always press Backspace to restart the current level.");
        labels.add(m);

        Button back = new Button("Ready to battle!", BACK_X, BACK_Y, BACK_SIZE);
        back.onClick(() -> sm.setScreen(new Menu()));
        clickables.add(back);
    }
}
