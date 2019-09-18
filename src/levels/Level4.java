package levels;

import levels.elements.Ballista;
import levels.elements.Catapult;
import levels.elements.Defence;
import levels.elements.Question;
import units.Archer;
import units.Footman;

import static main.ScreenManager.sm;

public class Level4 extends ALevel {

    public Level4() {
        super(Level5.class);
    }

    @Override
    public Question createQuestion() {
        return new Question("The previous ruler of this castle forced his slaves to work restlessly.",
                "That's terrible! Let's show these people some good will!",
                "Seems like a practical guy.", 100);
    }

    @Override
    public void fillAvailable() {
        soldierAmounts.put(Footman.class, 8);
        soldierAmounts.put(Archer.class, 8);
        creatureAmounts.put("priest", 3);
        creatureAmounts.put("angel", 2);
        creatureAmounts.put("warlock", 3);
        creatureAmounts.put("necromancer", 2);
    }

    @Override
    public void addDefences() {
        float y = defences.get(0).position.y;
        float space = sm.getWidth() * Defence.TOWER_WIDTH * 2;
        for (float x = space / 2 + space / 4; x < sm.getWidth() - space; x += space) {
            defences.add(new Catapult(x, y));
        }

        y += sm.getHeight() * Defence.TOWER_HEIGHT * 1.2f;
        for (float x = space / 4; x < sm.getWidth(); x += space) {
            defences.add(new Ballista(x, y));
        }
    }
}
