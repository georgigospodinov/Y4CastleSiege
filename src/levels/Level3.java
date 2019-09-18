package levels;

import levels.elements.Ballista;
import levels.elements.Catapult;
import levels.elements.Defence;
import levels.elements.Question;
import units.Archer;
import units.Footman;

import static main.ScreenManager.sm;

public class Level3 extends ALevel {

    public Level3() {
        super(Level4.class);
    }

    @Override
    public Question createQuestion() {
        return new Question("It seems that the former king has been a generous man.\n" +
                "He used to share the royal food supplies with the common folk.",
                "That's a great idea!\nLet's apply it to all the castles under my rule.",
                "Stop this practice.\nCommoners should not taste royal food.", 75);
    }

    @Override
    public void fillAvailable() {
        soldierAmounts.put(Footman.class, 6);
        soldierAmounts.put(Archer.class, 6);
        creatureAmounts.put("priest", 2);
        creatureAmounts.put("angel", 2);
        creatureAmounts.put("warlock", 2);
        creatureAmounts.put("necromancer", 1);
    }

    @Override
    public void addDefences() {
        float y = sm.getHeight() * 2.15f * Defence.TOWER_HEIGHT;
        Catapult catapult1 = new Catapult(0f, y);
        Catapult catapult2 = new Catapult((float) (sm.getWidth() - catapult1.width), y);
        defences.add(catapult1);
        defences.add(catapult2);
        y += catapult1.height;

        float space = sm.getWidth() * Defence.TOWER_WIDTH * 2;
        for (float x = 0; x < sm.getWidth(); x += space) {
            defences.add(new Ballista(x, y));
        }
    }
}
