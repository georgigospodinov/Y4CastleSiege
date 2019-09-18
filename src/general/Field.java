package general;

import levels.ALevel;
import levels.Level1;
import magical.Spell;
import screens.Play;
import units.Soldier;
import units.Unit;
import units.Zombie;

import java.util.Iterator;
import java.util.LinkedList;

import static main.Run.L;
import static main.ScreenManager.sm;

public class Field {
    public static LinkedList<Unit> units = new LinkedList<>();
    public static LinkedList<Unit> dead = new LinkedList<>();
    public static ALevel level = new Level1();
    public static Spell castingSpell = null;

    public static void createUnit(int x, int y, Class<? extends Soldier> soldierClass) {
        try {
            Unit u = new Unit(10, soldierClass, x, y);
            units.add(u);
        }
        catch (Exception e) {
            L.log(e);
        }
    }

    public static void render() {
        if (!(sm.getScreen() instanceof Play))
            return;
        level.render();
        for (Iterator<Unit> iterator = units.iterator(); iterator.hasNext(); ) {
            Unit unit = iterator.next();
            if (unit.isDead()) {
                iterator.remove();
                if (!unit.type.equals(Zombie.class)) {
                    dead.addLast(unit);
                }
            }
        }
        units.forEach(Unit::render);

        if (castingSpell != null)
            castingSpell.render();
    }

    public static void setFocusPoint(float x, float y) {
        units.forEach(u -> u.moveInFormation(x, y));
    }
}
