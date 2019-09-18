package levels;

import levels.elements.Ballista;
import levels.elements.Question;
import units.Archer;
import units.Footman;

import static main.ScreenManager.sm;

public class Level1 extends ALevel {

    public Level1() {
        super(Level2.class);
    }

    @Override
    public Question createQuestion() {
        return new Question("It appears that the previous monarch has had high tax rates,\n" +
                "the people here barely have enough to eat.\nWhat shall You do, Your majesty?",
                "Lower the taxes. This hunger must come to an end.",
                "Keep the taxes high. They've survived this long...", 50);
    }

    @Override
    public void fillAvailable() {
        soldierAmounts.put(Archer.class, 2);
        soldierAmounts.put(Footman.class, 2);
    }

    @Override
    public void addDefences() {
        Ballista ballista1 = new Ballista(sm.getWidth() * 0.33f, sm.getHeight() * 0.33f);
        Ballista ballista2 = new Ballista(sm.getWidth() * 0.67f, sm.getHeight() * 0.33f);
        defences.add(ballista1);
        defences.add(ballista2);
    }
}
