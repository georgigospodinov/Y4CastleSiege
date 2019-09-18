package levels;

import levels.elements.Ballista;
import levels.elements.Catapult;
import levels.elements.Defence;
import levels.elements.Question;
import screens.Play;
import units.Archer;
import units.Footman;

import static main.ScreenManager.sm;

public class Level5 extends ALevel {
    @Override
    public Question createQuestion() {
        return new Question("The previous ruler had been sending some of his troop east,\n" +
                "to assist another castle in defending against orc invasions.",
                "Better keep it up then, we don't want any orc invasions.",
                "Leave them be, if orcs come our way, we can handle them easily.", 80);
    }

    @Override
    public void fillAvailable() {
        soldierAmounts.put(Footman.class, 11);
        soldierAmounts.put(Archer.class, 9);
        creatureAmounts.put("priest", 4);
        creatureAmounts.put("angel", 2);
        creatureAmounts.put("warlock", 4);
        creatureAmounts.put("necromancer", 2);
    }

    @Override
    public void addDefences() {
        float x = 0;
        float h = sm.getHeight() * Defence.TOWER_HEIGHT * 1.2f;
        Defence first = defences.get(0);
        float initialY = first.position.y + first.height;
        float endY = Play.SPAWN_LIMIT;
        for (float y = initialY; y < endY; y += h) {
            defences.add(new Catapult(x, y));
        }
        x = sm.getWidth() - first.width;
        for (float y = initialY; y < endY; y += h) {
            defences.add(new Catapult(x, y));
        }

        float y = first.position.y;
        float space = sm.getWidth() * Defence.TOWER_WIDTH * 2;
        for (x = first.position.x + space; x < defences.get(1).position.x - space / 2; x += space) {
            defences.add(new Ballista(x, y));
        }

        x = sm.getWidth() * 0.5f - space / 4;
        for (y = first.position.y + h; y < endY; y += h) {
            defences.add(new Ballista(x, y));
        }
    }
}
