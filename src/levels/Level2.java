package levels;

import levels.elements.Ballista;
import levels.elements.Catapult;
import levels.elements.Question;
import units.Archer;
import units.Footman;

import static main.ScreenManager.sm;

public class Level2 extends ALevel {

    public Level2() {
        super(Level3.class);
    }

    @Override
    public Question createQuestion() {
        return new Question("People here live in fear due to a terrible law.\n" +
                "Burglary is, in all cases, punished by cutting off the hands of the thief.\n" +
                "What will You do, Your majesty?", "Remove the law. There are better ways to handle crime.",
                "Keep the law. People must be thought not to steal.", 65);
    }

    @Override
    public void fillAvailable() {
        soldierAmounts.put(Footman.class, 4);
        soldierAmounts.put(Archer.class, 4);
        creatureAmounts.put("priest", 2);
        creatureAmounts.put("warlock", 2);
    }

    @Override
    public void addDefences() {
        Catapult middle = new Catapult(sm.getWidth() * 0.5f - defences.get(0).width / 2f, castle.height + 10f);
        Ballista ballistaL = new Ballista(sm.getWidth() * 0.33f, sm.getHeight() * 0.5f);
        Ballista ballistaR = new Ballista(sm.getWidth() * 0.67f, sm.getHeight() * 0.5f);
        defences.add(middle);
        defences.add(ballistaL);
        defences.add(ballistaR);
    }
}
