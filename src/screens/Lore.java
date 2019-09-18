package screens;

import screens.components.Button;
import screens.components.Message;

import static main.ScreenManager.sm;

public class Lore extends AScreen {

    public Lore() {
        Message m = new Message("Your time has finally come!", DETAILS_X, DETAILS_Y, DETAILS_SIZE);
        m.addLine("Lead an army and conquer all castles that you may find!");
        m.addLine("You can be a liberator or a tyrant.");
        m.addLine("Your moral choices will determine what magical creatures you will meet.");
        m.addLine("Sparing your soldiers and the enemy towers increases moral.");
        m.addLine("Destroying towers decreases moral.");
        labels.add(m);

        Button back = new Button("Ready to battle!", BACK_X, BACK_Y, BACK_SIZE);
        back.onClick(() -> sm.setScreen(new Menu()));
        clickables.add(back);
    }
}
