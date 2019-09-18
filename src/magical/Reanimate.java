package magical;

import general.Field;
import processing.core.PImage;
import units.Unit;
import units.Zombie;

import java.util.Iterator;
import java.util.LinkedList;

import static main.Run.L;
import static main.Run.P;
import static main.ScreenManager.sm;

public class Reanimate extends Spell {
    public static final float RANGE = P.getFloat("reanimate range") * sm.getWidth();

    public Reanimate(PImage castingImage) {
        super(castingImage);
    }

    @Override
    public void takeEffect() {
        LinkedList<Unit> freshZombies = new LinkedList<>();
        for (Unit unit : Field.units) {
            double d = targetLocation.dist(unit.calcCenterPoint());
            // Outside of range.
            if (d > RANGE) continue;

            try {
                Unit zombies = new Unit(unit.countDead(), Zombie.class, targetLocation.x, targetLocation.y);
                freshZombies.add(zombies);
                unit.unhealable();
            }
            catch (Exception e) {
                L.log(e);
            }
        }
        for (Iterator<Unit> iterator = Field.dead.iterator(); iterator.hasNext(); ) {
            Unit unit = iterator.next();
            double d = targetLocation.dist(unit.calcCenterPoint());

            // Outside of range.
            if (d > RANGE) continue;

            try {
                Unit zombies = new Unit(unit.countDead(), Zombie.class, targetLocation.x, targetLocation.y);
                freshZombies.add(zombies);
                iterator.remove();
            }
            catch (Exception e) {
                L.log(e);
            }
        }

        Field.units.addAll(freshZombies);

    }
}
